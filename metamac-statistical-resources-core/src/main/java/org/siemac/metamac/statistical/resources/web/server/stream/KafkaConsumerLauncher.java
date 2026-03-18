package org.siemac.metamac.statistical.resources.web.server.stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListOffsetsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.OffsetSpec;
import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.UnknownMemberIdException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;
import org.siemac.metamac.srm.core.stream.message.DataStructureDefinitionAvro;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.siemac.metamac.statistical.operations.core.stream.messages.OperationAvro;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConfigurationConstants;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourcesRoleEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.core.invocation.service.NoticesRestInternalService;
import org.siemac.metamac.statistical.resources.core.notices.ServiceNoticeAction;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import es.ibestat.jaxi.stream.messages.DatasetAvro;
import es.ibestat.jaxi.stream.messages.PublicationAvro;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

@Component
public class KafkaConsumerLauncher implements ApplicationListener<ContextRefreshedEvent> {

    protected static final Log                LOGGER                                                       = LogFactory.getLog(KafkaConsumerLauncher.class);

    private static final String               MAX_POOL_MSG                                                 = "We have set a poll of 1 message at most. This error can not be given.";
    private String                            externalDatasetPublicationTopicName;
    private String                            externalCollectionPublicationTopicName;
    @Autowired
    private StatisticalResourcesConfiguration statisticalResourcesConfiguration;

    @Autowired
    private ThreadPoolTaskExecutor            threadPoolTaskExecutor;

    @Autowired
    private NoticesRestInternalService        noticesRestInternalService;

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    private Map<String, Future<?>>            futuresMap;

    private Cache                             kafkaFailedMessagesCache;
    private static final String               CONSUMER_EXTERNAL_DATASET_PUBLICATION_MESSAGES_1_NAME     = "statistical_resources_consumer_jaxi_publication_1";
    private static final String               CONSUMER_EXTERNAL_DATASET_PUBLICATION_CUSTOM_MESSAGE_NAME = "statistical_resources_consumer_jaxi_publication_2";
    private static final String               CONSUMER_CODELIST_PUBLICATION_MESSAGES_1_NAME             = "statistical_resources_consumer_jaxi_publication_1";
    private static final String               CONSUMER_CONCEPT_SCHEME_PUBLICATION_MESSAGES_1_NAME       = "statistical_resources_consumer_codelist_publication_1";
    private static final String               CONSUMER_DSD_PUBLICATION_MESSAGES_1_NAME                  = "statistical_resources_consumer_dsd_publication_1";
    private static final String               CONSUMER_OPERATION_PUBLICATION_MESSAGES_1_NAME            = "statistical_resources_consumer_operation_publication_1";
    private static final String               CONSUMER_DATASET_PUBLICATION_MESSAGES_1_NAME              = "statistical_resources_consumer_dataset_publication_1";
    private static final String               CONSUMER_EXTERNAL_COLLECTION_PUBLICATION_MESSAGES_1_NAME  = "statistical_resources_consumer_jaxi_collection_publication_1";
    private static final String               CONSUMER_EXTERNAL_COLLECTION_PUBLICATION_CUSTOM_MESSAGE_NAME = "statistical_resources_consumer_jaxi_collection_publication_2";
    private static final String               KAFKA_FAILED_CACHE_NAME                                   = "kafkaFailed";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext ac = event.getApplicationContext();
        if (ac.getParent() == null) {
            // @formatter:off
            try {
                List<NewTopic> availableTopics = new ArrayList<>();
                availableTopics = KafkaInitializeConsumerTopics.propagateCreationOfTopics(statisticalResourcesConfiguration);
                prepareFailedMessageCache();

                futuresMap = new HashMap<>();
                
                futuresMap.put(CONSUMER_CODELIST_PUBLICATION_MESSAGES_1_NAME, startConsumerForCodelistTopic(ac));
                futuresMap.put(CONSUMER_CONCEPT_SCHEME_PUBLICATION_MESSAGES_1_NAME, startConsumerForConceptSchemeTopic(ac));
                futuresMap.put(CONSUMER_DSD_PUBLICATION_MESSAGES_1_NAME, startConsumerForDsdTopic(ac));
                futuresMap.put(CONSUMER_OPERATION_PUBLICATION_MESSAGES_1_NAME, startConsumerForOperationTopic(ac));
                futuresMap.put(CONSUMER_DATASET_PUBLICATION_MESSAGES_1_NAME, startConsumerForDatasetTopic(ac));

                String externalPublicationTopicName = getExternalDatasetPublicationTopic();
                if (externalPublicationTopicName != null && Boolean.TRUE.equals(checkIsAvailableTopic(availableTopics, externalPublicationTopicName))) {
                futuresMap.put(CONSUMER_EXTERNAL_DATASET_PUBLICATION_MESSAGES_1_NAME, startConsumerForExternalDatasetPublicationTopic(ac, externalPublicationTopicName, CONSUMER_EXTERNAL_DATASET_PUBLICATION_MESSAGES_1_NAME));
                }

                externalCollectionPublicationTopicName = getExternalCollectionPublicationTopic();
                if (externalCollectionPublicationTopicName != null && Boolean.TRUE.equals(checkIsAvailableTopic(availableTopics, externalCollectionPublicationTopicName))) {
                futuresMap.put(CONSUMER_EXTERNAL_COLLECTION_PUBLICATION_MESSAGES_1_NAME, startConsumerForExternalCollectionPublicationTopic(ac, externalCollectionPublicationTopicName, CONSUMER_EXTERNAL_COLLECTION_PUBLICATION_MESSAGES_1_NAME));
                }
                
                if (!futuresMap.isEmpty()) {
                startKeepAliveKafkaThread(ac);
                }
            } catch (Exception e) {
                LOGGER.error(e, e.getCause());
            }
        }
            // @formatter:on
    }

    private Boolean checkIsAvailableTopic(List<NewTopic> availableTopics, String topicName) {
        for (NewTopic topic : availableTopics) {
            if (topicName.equals(topic.name())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private String getExternalDatasetPublicationTopic() {
        try {
            // The topic can not be enabled in some environments.
            return statisticalResourcesConfiguration.retrieveKafkaTopicExternalDatasetPublication();
        } catch (Exception e) {
            LOGGER.info("getExternalDatasetPublicationTopic not found. Check if must exists in common metadata");
        }
        return null;
    }

    private String getExternalCollectionPublicationTopic() {
        try {
            // The topic can not be enabled in some environments.
            return statisticalResourcesConfiguration.retrieveKafkaTopicExternalCollectionPublication();
        } catch (Exception e) {
            LOGGER.info("retrieveKafkaTopicExternalCollectionPublication not found. Check if must exists in common metadata");
        }
        return null;
    }

    private Long getLastConsumerOffset(String externalPublicationTopicGroup) throws MetamacException {

        Properties properties = new Properties();
        properties.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 10000);
        properties.put(AdminClientConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, 10000);
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, statisticalResourcesConfiguration.retrieveKafkaBootStrapServers());

        if (externalPublicationTopicGroup != null) {

            try (AdminClient adminClient = AdminClient.create(properties)) {

                Map<TopicPartition, OffsetSpec> requestLatestOffsets = new HashMap<>();

                Map<TopicPartition, OffsetAndMetadata> offsets = adminClient.listConsumerGroupOffsets(externalPublicationTopicGroup).partitionsToOffsetAndMetadata().get();

                for (TopicPartition tp : offsets.keySet()) {
                    requestLatestOffsets.put(tp, OffsetSpec.latest());
                }
                Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> earliestOffsets = adminClient.listOffsets(requestLatestOffsets).all().get();

                for (Map.Entry<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> e : earliestOffsets.entrySet()) {
                    LOGGER.info("get topic-partition " + e.getKey() + " last offset " + e.getValue().offset());
                    return e.getValue().offset();
                }

            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error("Failed to get the offsets committed by group " + externalPublicationTopicGroup + " with error " + e.getMessage());
                if (e.getCause() instanceof UnknownMemberIdException)
                    LOGGER.error("Check if consumer group is still active.");
            }
        }
        return 0L;
    }

    public void createCustomConsumer(StatisticalResourceTypeEnum statisticalResourceTypeEnum) throws MetamacException {

        switch (statisticalResourceTypeEnum) {
            case COLLECTION:
                createCollectionCustomConsumer(externalCollectionPublicationTopicName, CONSUMER_EXTERNAL_COLLECTION_PUBLICATION_CUSTOM_MESSAGE_NAME);
                break;
            case DATASET:
                createDatasetCustomConsumer(externalDatasetPublicationTopicName, CONSUMER_EXTERNAL_DATASET_PUBLICATION_CUSTOM_MESSAGE_NAME);
                break;
            default:
                throw new IllegalArgumentException("Kafka consumer for this resource is not available");

        }
    }

    public void createCollectionCustomConsumer(String externalPublicationTopicName, String customMessage) throws MetamacException {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<>();
        if (externalPublicationTopicName != null) {
            try (KafkaConsumer<String, PublicationAvro> consumer = createCustomCollectionConsumerFromCurrentOffset(externalPublicationTopicName, customMessage);) {
                updateExternalPublication(statisticalResourcesConfiguration.retrieveKafkaExternalCollectionPublicationMessagesGroup(), externalPublicationTopicName, consumer, exceptionItems);
            } catch (Exception e) {
                LOGGER.error(e, e.getCause());
            } finally {
                sendErrorNotification(exceptionItems, ServiceExceptionType.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_PUBLICATION_KAFKA_PRINCIPAL_ERROR,
                        ServiceNoticeAction.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_COLLECTION_PUBLICATION);
            }

        }

    }

    public void createDatasetCustomConsumer(String externalPublicationTopicName, String customMessage) throws MetamacException {
        List<MetamacExceptionItem> exceptionItems = new ArrayList<>();
        if (externalPublicationTopicName != null) {
            try (KafkaConsumer<String, DatasetAvro> consumer = createCustomDatasetConsumerFromCurrentOffset(externalPublicationTopicName, customMessage);) {
                updateExternalPublication(statisticalResourcesConfiguration.retrieveKafkaExternalDatasetPublicationMessagesGroup(), externalPublicationTopicName, consumer, exceptionItems);
            } catch (Exception e) {
                LOGGER.error(e, e.getCause());
            } finally {
                sendErrorNotification(exceptionItems, ServiceExceptionType.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_PUBLICATION_KAFKA_PRINCIPAL_ERROR,
                        ServiceNoticeAction.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_PUBLICATION_DATASET);
            }

        }

    }

    private void updateExternalPublication(String externalPublicationTopicGroup, String externalPublicationTopicName, KafkaConsumer<String, ?> consumer, List<MetamacExceptionItem> exceptionItems)
            throws MetamacException {
        int it = 0;
        Long latestOffset = getLastConsumerOffset(externalPublicationTopicGroup);

        if (latestOffset <= 0L) {
            noticesRestInternalService.createExternalPublicationUpdateErrorBackgroundNotification(externalPublicationTopicName);
        }

        externalPublicationConsumerConfig(externalPublicationTopicName, consumer);

        boolean keepOnReading = true;

        Map<Integer, Long> pendigOffsetsToCommit = new HashMap<Integer, Long>(); // K:partition, V:offset

        while (keepOnReading) {
            ConsumerRecords<String, ?> records = consumer.poll(100);

            if (it > latestOffset) {
                LOGGER.info("last consumer topic external publication iteration " + latestOffset);
                keepOnReading = false;
            }

            it++;

            if (records.count() > 1) {
                LOGGER.error(MAX_POOL_MSG);
                throw new RuntimeException(MAX_POOL_MSG);
            }

            if (!records.isEmpty()) {

                // Process resources
                ConsumerRecord<String, ?> record = records.iterator().next();

                if (record.offset() >= latestOffset - 1) {
                    LOGGER.info("The external publication update has finished correctly. Consumer topic external publication last offset " + latestOffset);
                    keepOnReading = false;
                }

                if (!removePendingOffsets(consumer, record, pendigOffsetsToCommit)) {
                    callFacadeBusinessLogicUpdateCache(externalPublicationTopicName, consumer, record, pendigOffsetsToCommit, exceptionItems);
                }
            }
        }

    }

    private void sendErrorNotification(List<MetamacExceptionItem> exceptionItems, CommonServiceExceptionType principalMessage, String errorSubjectMessage) {
        if (!exceptionItems.isEmpty()) {
            MetamacException metamacException = new MetamacException();
            metamacException.getExceptionItems().addAll(exceptionItems);
            metamacException.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_PUBLICATION_KAFKA_PRINCIPAL_ERROR));
            noticesRestInternalService.createErrorBackgroundNotification(ServiceNoticeAction.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_PUBLICATION_DATASET, metamacException);
        }
    }

    private boolean removePendingOffsets(KafkaConsumer<String, ?> consumer, ConsumerRecord<String, ?> record, Map<Integer, Long> pendigOffsetsToCommit) {
        if (pendigOffsetsToCommit.containsKey(record.partition()) && record.offset() == pendigOffsetsToCommit.get(record.partition())) {
            LOGGER.debug("The current message already processed successfully");
            if (commitSync(consumer, record)) {
                pendigOffsetsToCommit.remove(record.partition());
                removeFromErrorCacheMessagesIfNeccesary(record);
            }
            return true;
        }
        return false;
    }

    private void externalPublicationConsumerConfig(String externalPublicationTopicName, KafkaConsumer<String, ?> consumer) {
        consumer.subscribe(Collections.singleton(externalPublicationTopicName), new ConsumerRebalanceListener() {

            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                consumer.seekToBeginning(partitions);
            }
        });
    }

    private void callFacadeBusinessLogicUpdateCache(String externalPublicationTopicName, KafkaConsumer<String, ?> consumer, ConsumerRecord<String, ?> record, Map<Integer, Long> pendigOffsetsToCommit,
            List<MetamacExceptionItem> exceptions) {
        StringBuilder logMessageBldr = new StringBuilder("Received message from Kafka -> Topic Name: ");
        // @formatter:off
        logMessageBldr
            .append(externalPublicationTopicName)
            .append(", Partition: ").append(record.partition())
            .append(", Offset: ").append(record.offset())
            .append(", TimestampType: ").append(record.timestampType())
            .append(", Timestamp: ").append(record.timestamp())
            .append(" [").append(new DateTime(record.timestamp(), DateTimeZone.forID("Atlantic/Canary"))).append("]");
        // @formatter:on
        String logMessage = logMessageBldr.toString();

        pendigOffsetsToCommit.put(record.partition(), record.offset());

        LOGGER.info(logMessage);

        try {
            ServiceContext serviceContext = createServiceContext(logMessage);

            executeUpdateGeographicCoverageExternalPublicationCache(externalPublicationTopicName, serviceContext, consumer, record, exceptions);

        } catch (MetamacException e) {
            LOGGER.error("Unable to process resource received from Kafka. The business of application has failed", e);
            exceptions.addAll(e.getExceptionItems());
            LOGGER.error("Process the next resource and discard the current message, key of message: " + record.key());
        } catch (Exception e) {
            LOGGER.error("Unable to process resource received from Kafka. The business of application has failed", e);
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.UPDATE_GEOCOVERAGE_CACHE_EXTERNAL_PUBLICATION_KAFKA_ERROR, record.key()));
            LOGGER.error("Process the next resource and discard the current message, key of message: " + record.key());
        }
    }

    private void executeUpdateGeographicCoverageExternalPublicationCache(String externalPublicationTopicName, ServiceContext serviceContext, KafkaConsumer<String, ?> consumer,
            ConsumerRecord<String, ?> record, List<MetamacExceptionItem> exceptions) throws MetamacException {

        String urn = null;
        SpecificRecordBase recordBase = null;
        if (externalDatasetPublicationTopicName.equals(externalPublicationTopicName)) {
            ConsumerRecord<String, DatasetAvro> datasetRecord = (ConsumerRecord<String, DatasetAvro>) record;
            urn = datasetRecord.value() != null ? datasetRecord.value().getUrn() : null;
            recordBase = datasetRecord.value();
        } else if (externalCollectionPublicationTopicName.equals(externalPublicationTopicName)) {
            ConsumerRecord<String, PublicationAvro> publicationRecord = (ConsumerRecord<String, PublicationAvro>) record;
            urn = publicationRecord.value() != null ? publicationRecord.value().getUrn() : null;
            recordBase = publicationRecord.value();
        }

        if (urn == null) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.UPDATE_GEOCOVERAGE_CACHE_RESOURCE_FROM_EXTERNAL_PUBLICATION_ERROR_STREAM_NO_VALID, record.key()));
        } else {

            statisticalResourcesServiceFacade.updateGeographicCoverageExternalPublicationCache(serviceContext, recordBase);
            commitSync(consumer, record);
        }
    }

    private void removeFromErrorCacheMessagesIfNeccesary(ConsumerRecord<String, ?> record) {
        if (kafkaFailedMessagesCache.isKeyInCache(record.key())) {
            kafkaFailedMessagesCache.remove(record.key());
        }
    }

    private ServiceContext createServiceContext(String logMessage) {
        ServiceContext serviceContext = new ServiceContext("kafka-jaxi-publication-received", logMessage.toString(), "metamac-statistical-resources-core");
        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        metamacPrincipal.setUserId(serviceContext.getUserId());
        metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(StatisticalResourcesRoleEnum.ADMINISTRADOR.getName(), StatisticalResourcesConstants.APPLICATION_ID, null));
        serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
        return serviceContext;
    }

    private boolean commitSync(KafkaConsumer<String, ?> consumer, ConsumerRecord<String, ?> record) {
        try {
            consumer.commitSync(Collections.singletonMap(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1)));
            LOGGER.debug("Commited message: " + record.partition() + " : " + record.offset());
        } catch (CommitFailedException e) {
            LOGGER.debug("The message processing takes longer than the session timeout. The coordinator kicks the consumer out of the group (rebalanced)");
            return false;
        }
        return true;
    }

    private KafkaConsumer<String, DatasetAvro> createCustomDatasetConsumerFromCurrentOffset(String topic, String clientId) throws MetamacException {
        KafkaConsumer<String, DatasetAvro> kafkaConsumer = new KafkaConsumer<>(
                getCustomonsumerProperties(clientId, statisticalResourcesConfiguration.retrieveKafkaCustomExternalDatasetPublicationMessagesGroup()));
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        return kafkaConsumer;
    }

    private KafkaConsumer<String, PublicationAvro> createCustomCollectionConsumerFromCurrentOffset(String topic, String clientId) throws MetamacException {
        KafkaConsumer<String, PublicationAvro> kafkaConsumer = new KafkaConsumer<>(
                getCustomonsumerProperties(clientId, statisticalResourcesConfiguration.retrieveKafkaCustomExternalCollectionPublicationMessagesGroup()));
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        return kafkaConsumer;
    }

    private Properties getCustomonsumerProperties(String clientId, String group) throws MetamacException {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, statisticalResourcesConfiguration.retrieveKafkaBootStrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroDeserializer.class);

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // Default is True
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10000); // 10 s
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 900000); // 15 min, Max time for Bussiness Logic execution of consumer thread
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1); // The maximum number of records returned in a single call to poll()
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OffsetResetStrategy.LATEST.toString().toLowerCase()); // Policy to follow when there are no confirmed offset

        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, statisticalResourcesConfiguration.retrieveKafkaSchemaRegistryUrl());
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

        return props;
    }

    public void startKeepAliveKafkaThread(ApplicationContext context) throws MetamacException {
        KeepAliveKafkaThread keepAliveKafkaThread = new KeepAliveKafkaThread();
        threadPoolTaskExecutor.execute(keepAliveKafkaThread);
    }

    private void prepareFailedMessageCache() {
        CacheManager cacheManager = CacheManager.getInstance();
        cacheManager.addCache(KAFKA_FAILED_CACHE_NAME);
        Cache cache = cacheManager.getCache(KAFKA_FAILED_CACHE_NAME);
        kafkaFailedMessagesCache = cache;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Future<?> startConsumerForConceptSchemeTopic(ApplicationContext context) throws MetamacException {
        String topicConceptSchemePublication = statisticalResourcesConfiguration.retrieveKafkaTopicConceptSchemesPublication();
        KafkaConsumerThread<ConceptSchemeAvro> consumerThread = (KafkaConsumerThread) context.getBean("kafkaConsumerThread");
        KafkaConsumer<String, ConceptSchemeAvro> consumerFromBegin = createConceptSchemeConsumerFromCurrentOffset(topicConceptSchemePublication, CONSUMER_CONCEPT_SCHEME_PUBLICATION_MESSAGES_1_NAME);
        consumerThread.setConsumer(consumerFromBegin);
        consumerThread.setTopicName(topicConceptSchemePublication);
        consumerThread.setStatisticalServiceFacade(statisticalResourcesServiceFacade);
        consumerThread.setNoticesRestInternalService(noticesRestInternalService);
        consumerThread.setKafkaFailedMessagesCache(kafkaFailedMessagesCache);
        return threadPoolTaskExecutor.submit(consumerThread);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Future<?> startConsumerForDsdTopic(ApplicationContext context) throws MetamacException {
        String topicDsdPublication = statisticalResourcesConfiguration.retrieveKafkaTopicDsdPublication();
        KafkaConsumerThread<DataStructureDefinitionAvro> consumerThread = (KafkaConsumerThread) context.getBean("kafkaConsumerThread");
        KafkaConsumer<String, DataStructureDefinitionAvro> consumerFromBegin = createDsdConsumerFromCurrentOffset(topicDsdPublication, CONSUMER_DSD_PUBLICATION_MESSAGES_1_NAME);
        consumerThread.setConsumer(consumerFromBegin);
        consumerThread.setTopicName(topicDsdPublication);
        consumerThread.setStatisticalServiceFacade(statisticalResourcesServiceFacade);
        consumerThread.setNoticesRestInternalService(noticesRestInternalService);
        consumerThread.setKafkaFailedMessagesCache(kafkaFailedMessagesCache);
        return threadPoolTaskExecutor.submit(consumerThread);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Future<?> startConsumerForOperationTopic(ApplicationContext context) throws MetamacException {
        String topicOperationPublication = statisticalResourcesConfiguration.retrieveKafkaTopicOperationsPublication();
        KafkaConsumerThread<OperationAvro> consumerThread = (KafkaConsumerThread) context.getBean("kafkaConsumerThread");
        KafkaConsumer<String, OperationAvro> consumerFromBegin = createOperationConsumerFromCurrentOffset(topicOperationPublication, CONSUMER_OPERATION_PUBLICATION_MESSAGES_1_NAME);
        consumerThread.setConsumer(consumerFromBegin);
        consumerThread.setTopicName(topicOperationPublication);
        consumerThread.setStatisticalServiceFacade(statisticalResourcesServiceFacade);
        consumerThread.setNoticesRestInternalService(noticesRestInternalService);
        consumerThread.setKafkaFailedMessagesCache(kafkaFailedMessagesCache);
        return threadPoolTaskExecutor.submit(consumerThread);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Future<?> startConsumerForCodelistTopic(ApplicationContext context) throws MetamacException {
        String topicCodelistPublication = statisticalResourcesConfiguration.retrieveKafkaTopicCodelistsPublication();
        KafkaConsumerThread<CodelistAvro> consumerThread = (KafkaConsumerThread) context.getBean("kafkaConsumerThread");
        KafkaConsumer<String, CodelistAvro> consumerFromBegin = createCodelistConsumerFromCurrentOffset(topicCodelistPublication, CONSUMER_CODELIST_PUBLICATION_MESSAGES_1_NAME);
        consumerThread.setConsumer(consumerFromBegin);
        consumerThread.setTopicName(topicCodelistPublication);
        consumerThread.setStatisticalServiceFacade(statisticalResourcesServiceFacade);
        consumerThread.setNoticesRestInternalService(noticesRestInternalService);
        consumerThread.setKafkaFailedMessagesCache(kafkaFailedMessagesCache);
        return threadPoolTaskExecutor.submit(consumerThread);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Future<?> startConsumerForDatasetTopic(ApplicationContext context) throws MetamacException {
        String topicDatasetPublication = statisticalResourcesConfiguration.retrieveKafkaTopicDatasetsPublication();
        KafkaConsumerThread<DatasetVersionAvro> consumerThread = (KafkaConsumerThread) context.getBean("kafkaConsumerThread");
        KafkaConsumer<String, DatasetVersionAvro> consumerFromBegin = createDatasetConsumerFromCurrentOffset(topicDatasetPublication, CONSUMER_DATASET_PUBLICATION_MESSAGES_1_NAME);
        consumerThread.setConsumer(consumerFromBegin);
        consumerThread.setTopicName(topicDatasetPublication);
        consumerThread.setStatisticalServiceFacade(statisticalResourcesServiceFacade);
        consumerThread.setNoticesRestInternalService(noticesRestInternalService);
        consumerThread.setKafkaFailedMessagesCache(kafkaFailedMessagesCache);
        return threadPoolTaskExecutor.submit(consumerThread);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Future<?> startConsumerForExternalCollectionPublicationTopic(ApplicationContext context, String externalPublicationTopicName, String clientId) throws MetamacException {
        String topicJaxiPublication = externalPublicationTopicName;
        KafkaConsumerThread<PublicationAvro> consumerThread = (KafkaConsumerThread) context.getBean("kafkaConsumerThread");
        KafkaConsumer<String, PublicationAvro> consumerFromBegin = createCollectionConsumerFromCurrentOffset(topicJaxiPublication, clientId);
        consumerThread.setConsumer(consumerFromBegin);
        consumerThread.setTopicName(topicJaxiPublication);
        consumerThread.setStatisticalServiceFacade(statisticalResourcesServiceFacade);
        consumerThread.setNoticesRestInternalService(noticesRestInternalService);
        consumerThread.setKafkaFailedMessagesCache(kafkaFailedMessagesCache);
        consumerThread.setIsJaxiConsumerDisabled(retrieveJaxiPublicationConsumerIsDisabled());
        return threadPoolTaskExecutor.submit(consumerThread);
    }

    private boolean retrieveJaxiPublicationConsumerIsDisabled() {
        try {
            return statisticalResourcesConfiguration.retrievePropertyBoolean(StatisticalResourcesConfigurationConstants.DISABLED_JAXI_PUBLICATIONS_CONSUMER);
        } catch (Exception e) {
            return false;
        }
    }

     @SuppressWarnings({"unchecked", "rawtypes"})
    private Future<?> startConsumerForExternalDatasetPublicationTopic(ApplicationContext context, String externalPublicationTopicName, String clientId) throws MetamacException {
        String topicJaxiPublication = externalPublicationTopicName;
        KafkaConsumerThread<DatasetAvro> consumerThread = (KafkaConsumerThread) context.getBean("kafkaConsumerThread");
        KafkaConsumer<String, DatasetAvro> consumerFromBegin = createConsumerFromCurrentOffset(topicJaxiPublication, clientId);
        consumerThread.setConsumer(consumerFromBegin);
        consumerThread.setTopicName(topicJaxiPublication);
        consumerThread.setStatisticalServiceFacade(statisticalResourcesServiceFacade);
        consumerThread.setNoticesRestInternalService(noticesRestInternalService);
        consumerThread.setKafkaFailedMessagesCache(kafkaFailedMessagesCache);
        return threadPoolTaskExecutor.submit(consumerThread);
    }

    private Properties getConsumerProperties(String clientId, String group) throws MetamacException {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, statisticalResourcesConfiguration.retrieveKafkaBootStrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroDeserializer.class);

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // Default is True
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10000); // 10 s
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 900000); // 15 min, Max time for Bussiness Logic execution of consumer thread
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1); // The maximum number of records returned in a single call to poll()
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OffsetResetStrategy.LATEST.toString().toLowerCase());

        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, statisticalResourcesConfiguration.retrieveKafkaSchemaRegistryUrl());
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

        return props;
    }

    private KafkaConsumer<String, DatasetAvro> createConsumerFromCurrentOffset(String topic, String clientId) throws MetamacException {
        KafkaConsumer<String, DatasetAvro> kafkaConsumer = new KafkaConsumer<>(
                getConsumerProperties(clientId, statisticalResourcesConfiguration.retrieveKafkaExternalDatasetPublicationMessagesGroup()));
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        return kafkaConsumer;
    }

   private KafkaConsumer<String, PublicationAvro> createCollectionConsumerFromCurrentOffset(String topic, String clientId) throws MetamacException {
        KafkaConsumer<String, PublicationAvro> kafkaConsumer = new KafkaConsumer<>(
                getConsumerProperties(clientId, statisticalResourcesConfiguration.retrieveKafkaExternalCollectionPublicationMessagesGroup()));
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        return kafkaConsumer;
    }

    private KafkaConsumer<String, CodelistAvro> createCodelistConsumerFromCurrentOffset(String topic, String clientId) throws MetamacException {
        KafkaConsumer<String, CodelistAvro> kafkaConsumer = new KafkaConsumer<>(
                getConsumerProperties(clientId, statisticalResourcesConfiguration.retrieveKafkaCustomCodelistPublicationMessagesGroup()));
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        return kafkaConsumer;
    }

    private KafkaConsumer<String, ConceptSchemeAvro> createConceptSchemeConsumerFromCurrentOffset(String topic, String clientId) throws MetamacException {
        KafkaConsumer<String, ConceptSchemeAvro> kafkaConsumer = new KafkaConsumer<>(
                getConsumerProperties(clientId, statisticalResourcesConfiguration.retrieveKafkaCustomConceptSchemePublicationMessagesGroup()));
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        return kafkaConsumer;
    }

    private KafkaConsumer<String, DataStructureDefinitionAvro> createDsdConsumerFromCurrentOffset(String topic, String clientId) throws MetamacException {
        KafkaConsumer<String, DataStructureDefinitionAvro> kafkaConsumer = new KafkaConsumer<>(
                getConsumerProperties(clientId, statisticalResourcesConfiguration.retrieveKafkaCustomDsdPublicationMessagesGroup()));
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        return kafkaConsumer;
    }

    private KafkaConsumer<String, OperationAvro> createOperationConsumerFromCurrentOffset(String topic, String clientId) throws MetamacException {
        KafkaConsumer<String, OperationAvro> kafkaConsumer = new KafkaConsumer<>(
                getConsumerProperties(clientId, statisticalResourcesConfiguration.retrieveKafkaCustomOperationPublicationMessagesGroup()));
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        return kafkaConsumer;
    }

    private KafkaConsumer<String, DatasetVersionAvro> createDatasetConsumerFromCurrentOffset(String topic, String clientId) throws MetamacException {
        KafkaConsumer<String, DatasetVersionAvro> kafkaConsumer = new KafkaConsumer<>(
                getConsumerProperties(clientId, statisticalResourcesConfiguration.retrieveKafkaCustomDatasetPublicationMessagesGroup()));
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        return kafkaConsumer;
    }

    class KeepAliveKafkaThread implements Runnable {

        @Override
        public void run() {

            if (externalDatasetPublicationTopicName != null || externalCollectionPublicationTopicName != null) {
                while (alwaysWithDelay(1000)) {

                    for (Map.Entry<String, Future<?>> entry : futuresMap.entrySet()) {
                        if (entry.getValue().isDone()) {
                            LOGGER.info("El consumidor " + entry.getKey() + " se ha desconectado. Planificando otro consumidor para el mismo Topic...");
                            try {
                                if (CONSUMER_EXTERNAL_DATASET_PUBLICATION_MESSAGES_1_NAME.equals(entry.getKey())) {
                                    futuresMap.put(CONSUMER_EXTERNAL_DATASET_PUBLICATION_MESSAGES_1_NAME, startConsumerForExternalDatasetPublicationTopic(
                                            ApplicationContextProvider.getApplicationContext(), externalDatasetPublicationTopicName, CONSUMER_EXTERNAL_DATASET_PUBLICATION_MESSAGES_1_NAME));
                                }

                                if (CONSUMER_EXTERNAL_COLLECTION_PUBLICATION_MESSAGES_1_NAME.equals(entry.getKey())) {

                                    futuresMap.put(CONSUMER_EXTERNAL_COLLECTION_PUBLICATION_MESSAGES_1_NAME, startConsumerForExternalCollectionPublicationTopic(
                                            ApplicationContextProvider.getApplicationContext(), externalCollectionPublicationTopicName, CONSUMER_EXTERNAL_COLLECTION_PUBLICATION_MESSAGES_1_NAME));
                                }

                                if (CONSUMER_CODELIST_PUBLICATION_MESSAGES_1_NAME.equals(entry.getKey())) {
                                    futuresMap.put(CONSUMER_CODELIST_PUBLICATION_MESSAGES_1_NAME, startConsumerForCodelistTopic(ApplicationContextProvider.getApplicationContext()));
                                }

                                if (CONSUMER_CONCEPT_SCHEME_PUBLICATION_MESSAGES_1_NAME.equals(entry.getKey())) {
                                    futuresMap.put(CONSUMER_CONCEPT_SCHEME_PUBLICATION_MESSAGES_1_NAME, startConsumerForConceptSchemeTopic(ApplicationContextProvider.getApplicationContext()));
                                }

                                if (CONSUMER_DSD_PUBLICATION_MESSAGES_1_NAME.equals(entry.getKey())) {
                                    futuresMap.put(CONSUMER_DSD_PUBLICATION_MESSAGES_1_NAME, startConsumerForDsdTopic(ApplicationContextProvider.getApplicationContext()));
                                }

                                if (CONSUMER_OPERATION_PUBLICATION_MESSAGES_1_NAME.equals(entry.getKey())) {
                                    futuresMap.put(CONSUMER_OPERATION_PUBLICATION_MESSAGES_1_NAME, startConsumerForDsdTopic(ApplicationContextProvider.getApplicationContext()));
                                }

                            } catch (Exception e) {
                                long retyrMS = 6000;
                                LOGGER.error("Imposible replanificar consumidores de Kafka. Volviendolo a intentar en " + retyrMS + "ms", e);
                                alwaysWithDelay(60000);
                            }
                        }
                    }
                }
            }
        }

        private boolean alwaysWithDelay(long timeout) {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                LOGGER.error(e);
            }
            return true;
        }
    }

}
