package org.siemac.metamac.statistical.resources.web.server.stream;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;
import org.siemac.metamac.srm.core.stream.message.DataStructureDefinitionAvro;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.siemac.metamac.statistical.operations.core.stream.messages.OperationAvro;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourcesRoleEnum;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.core.invocation.service.NoticesRestInternalService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import es.ibestat.jaxi.stream.messages.DatasetAvro;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

@Component
@Scope("prototype")
public class KafkaConsumerThread<T extends SpecificRecordBase> implements Runnable {

    protected static Log                      LOGGER       = LogFactory.getLog(KafkaConsumerThread.class);

    private static final String               MAX_POOL_MSG = "We have set a poll of 1 message at most. This error can not be given.";

    private KafkaConsumer<String, T>          consumer;
    private String                            topicName;
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;
    private NoticesRestInternalService        noticesRestInternalService;
    private Cache                             kafkaFailedMessagesCache;
    private boolean                           isJaxiConsumerDisabled = false;

    public void setConsumer(KafkaConsumer<String, T> consumer) {
        this.consumer = consumer;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setStatisticalServiceFacade(StatisticalResourcesServiceFacade statisticalResourcesServiceFacade) {
        this.statisticalResourcesServiceFacade = statisticalResourcesServiceFacade;
    }

    public void setNoticesRestInternalService(NoticesRestInternalService noticesRestInternalService) {
        this.noticesRestInternalService = noticesRestInternalService;
    }

    public void setKafkaFailedMessagesCache(Cache kafkaFailedMessagesCache) {
        this.kafkaFailedMessagesCache = kafkaFailedMessagesCache;
    }

    public void setIsJaxiConsumerDisabled(boolean isDisabled) {
        this.isJaxiConsumerDisabled = isDisabled;
    }

    @Override
    public void run() {
        LOGGER.info("Reading KAFKA topic: " + topicName);

        try {

            Map<Integer, Long> pendigOffsetsToCommit = new HashMap<Integer, Long>(); // K:partition, V:offset

            while (KafkaUtils.alwaysWithDelay()) {
                // Milliseconds, spent waiting in poll if data is not available in the buffer
                ConsumerRecords<String, T> records = consumer.poll(100);

                if (records.count() > 1) {
                    LOGGER.error(MAX_POOL_MSG);
                    throw new RuntimeException(MAX_POOL_MSG);
                }

                if (records.isEmpty()) {
                    continue;
                }

                // Process resources
                ConsumerRecord<String, T> record = records.iterator().next();

                if (this.isJaxiConsumerDisabled) {
                    commitSync(record);
                    return;
                }

                if (pendigOffsetsToCommit.containsKey(record.partition()) && record.offset() == pendigOffsetsToCommit.get(record.partition())) {
                    LOGGER.debug("Statistical resources. The current message already processed successfully");
                    if (commitSync(record)) {
                        pendigOffsetsToCommit.remove(record.partition());
                        removeFromErrorCacheMessagesIfNeccesary(record);
                    }
                    continue;
                }

                StringBuilder logMessageBldr = KafkaUtils.buildLogMessage("Statistical resources. Received message from Kafka -> Topic Name: ", topicName, record.partition(), record.offset(),
                        record.timestampType(), record.timestamp());
                String logMessage = logMessageBldr.toString();

                pendigOffsetsToCommit.put(record.partition(), record.offset());

                LOGGER.info(logMessageBldr);
                try {
                    ServiceContext serviceContext = createServiceContext(logMessage);

                    updateByKafkaMessage(serviceContext, record);

                    commitSync(record);
                } catch (Exception e) {
                    LOGGER.error("Statistical resources. Unable to process resource received from Kafka. The business of application has failed", e);

                    // Send a error notification, the error message will send only if not exist in error cache
                    sendErrorMessageIfNeccesary(record);

                    LOGGER.error("Statistical resources. Process the next resource and discard the current message, key of message: " + record.key());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Statistical resources. An error has occurred in the Kafka client. Finishing the client.", e);
        } finally {
            LOGGER.info("Statistical resources. Closing the consumer...");
            consumer.close();
        }
    }

    public void updateByKafkaMessage(ServiceContext ctx, ConsumerRecord<String, T> record) throws MetamacException {
        if (record.value() instanceof DatasetAvro) {
            statisticalResourcesServiceFacade.updateGeographicCoverageExternalPublicationVariableElementsCache(ctx, record.value());
        } else if (record.value() instanceof CodelistAvro || record.value() instanceof ConceptSchemeAvro) {
            statisticalResourcesServiceFacade.processSrmResourcesKafkaMessage(ctx, record.value());
        } else if (record.value() instanceof DataStructureDefinitionAvro) {
            statisticalResourcesServiceFacade.processSrmDsdKafkaMessage(ctx, record.value(), record.
                    timestamp());
        } else if (record.value() instanceof OperationAvro) {
            statisticalResourcesServiceFacade.processOperationKafkaMessage(ctx, record.value(), record.
                    timestamp());
        }
    }

    private ServiceContext createServiceContext(String logMessage) {
        ServiceContext serviceContext = new ServiceContext("kafka-jaxi-publication-received", logMessage, "metamac-statistical-resources-core");
        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        metamacPrincipal.setUserId(serviceContext.getUserId());
        metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(StatisticalResourcesRoleEnum.ADMINISTRADOR.getName(), StatisticalResourcesConstants.APPLICATION_ID, null));
        serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
        return serviceContext;
    }

    private boolean commitSync(ConsumerRecord<String, T> record) {
        try {
            consumer.commitSync(Collections.singletonMap(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1)));
            LOGGER.debug("Commited message: " + record.partition() + " : " + record.offset());
        } catch (CommitFailedException e) {
            LOGGER.debug("Statistical resources. The message processing takes longer than the session timeout. The coordinator kicks the consumer out of the group (rebalanced)");
            return false;
        }
        return true;
    }

    private void sendErrorMessageIfNeccesary(ConsumerRecord<String, T> record) {
        if (!kafkaFailedMessagesCache.isKeyInCache(record.key())) {
            Element element = new Element(record.key(), record.value());
            element.setEternal(true);
            kafkaFailedMessagesCache.put(element);
            noticesRestInternalService.createConsumerFromKafkaErrorBackgroundNotification(record.key());
        }
    }

    private void removeFromErrorCacheMessagesIfNeccesary(ConsumerRecord<String, T> record) {
        if (kafkaFailedMessagesCache.isKeyInCache(record.key())) {
            kafkaFailedMessagesCache.remove(record.key());
        }
    }

}
