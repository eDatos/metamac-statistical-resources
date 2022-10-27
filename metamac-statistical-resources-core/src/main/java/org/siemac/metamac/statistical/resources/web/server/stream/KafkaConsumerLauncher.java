package org.siemac.metamac.statistical.resources.web.server.stream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.core.invocation.service.NoticesRestInternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import es.ibestat.jaxi.stream.messages.DatasetAvro;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

@Component
public class KafkaConsumerLauncher implements ApplicationListener<ContextRefreshedEvent> {

    protected static final Log             LOGGER                  = LogFactory.getLog(KafkaConsumerLauncher.class);
    
    @Autowired
    private StatisticalResourcesConfiguration statisticalResourcesConfiguration;
    
    @Autowired
    private ThreadPoolTaskExecutor         threadPoolTaskExecutor;
    
    @Autowired
    private NoticesRestInternalService     noticesRestInternalService;
    
    @Autowired
    private StatisticalResourcesServiceFacade     statisticalResourcesServiceFacade;
    
    private Map<String, Future<?>>         futuresMap;
    
    private Cache                          kafkaFailedMessagesCache;
    private static final String            CONSUMER_JAXI_MESSAGES_1_NAME   = "statistical_resources_consumer_jaxi_publication_1";
    private static final String            KAFKA_FAILED_CACHE_NAME = "kafkaFailed";
    
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
                
                if (Boolean.TRUE.equals(checkIsAvailableTopic(availableTopics, statisticalResourcesConfiguration.retrieveKafkaExternalPublicationsTopicName()))) {
                futuresMap.put(CONSUMER_JAXI_MESSAGES_1_NAME, startConsumerForJaxiTopic(ac));
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
    
    public void createCustomConsumer() {
            KafkaConsumer<String, DatasetAvro> consumer = null;
            try {
                String topicJaxiPublication = statisticalResourcesConfiguration.retrieveKafkaExternalPublicationsTopicName();
                consumer = createConsumerFromCurrentOffset(topicJaxiPublication, CONSUMER_JAXI_MESSAGES_1_NAME);
                
                int numberOfMessagesToRead = 5;
                boolean keepOnReading = true;
                int numberOfMessagesReadSoFar = 0;
                
                while(keepOnReading){
                    ConsumerRecords<String, DatasetAvro> records =
                            consumer.poll(100);

                    for (ConsumerRecord<String, DatasetAvro> record : records){
                        numberOfMessagesReadSoFar += 1;
                        LOGGER.info("Key: " + record.key() + ", Value: " + record.value());
                        LOGGER.info("Partition: " + record.partition() + ", Offset:" + record.offset());
                        if (numberOfMessagesReadSoFar >= numberOfMessagesToRead){
                            keepOnReading = false; // to exit the while loop
                            break; // to exit the for loop
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error(e, e.getCause());
    } finally {
        LOGGER.info("Closing the consumer...");
        if (consumer != null) {
        consumer.close();
        }
    }
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
    private Future<?> startConsumerForJaxiTopic(ApplicationContext context) throws MetamacException {
        String topicJaxiPublication = statisticalResourcesConfiguration.retrieveKafkaExternalPublicationsTopicName();
        KafkaConsumerThread<DatasetAvro> consumerThread = (KafkaConsumerThread) context.getBean("kafkaConsumerThread");
        KafkaConsumer<String, DatasetAvro> consumerFromBegin = createConsumerFromCurrentOffset(topicJaxiPublication, CONSUMER_JAXI_MESSAGES_1_NAME);
        consumerThread.setConsumer(consumerFromBegin);
        consumerThread.setTopicName(topicJaxiPublication);
        consumerThread.setStatisticalServiceFacade(statisticalResourcesServiceFacade);
        consumerThread.setNoticesRestInternalService(noticesRestInternalService);
        consumerThread.setKafkaFailedMessagesCache(kafkaFailedMessagesCache);
        return threadPoolTaskExecutor.submit(consumerThread);
    }
    
    private Properties getConsumerProperties(String clientId) throws MetamacException {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, statisticalResourcesConfiguration.retrieveKafkaBootStrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, statisticalResourcesConfiguration.retrieveKafkaJaxiMessagesGroup());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroDeserializer.class);

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // Default is True
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10000); // 10 s
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 900000); // 15 min, Max time for Bussiness Logic execution of consumer thread
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1); // The maximum number of records returned in a single call to poll()
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OffsetResetStrategy.EARLIEST.toString().toLowerCase()); // Policy to follow when there are no confirmed offset

        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, statisticalResourcesConfiguration.retrieveKafkaSchemaRegistryUrl());
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

        return props;
    }
    
    private KafkaConsumer<String, DatasetAvro> createConsumerFromCurrentOffset(String topic, String clientId) throws MetamacException {
        KafkaConsumer<String, DatasetAvro> kafkaConsumer = new KafkaConsumer<>(getConsumerProperties(clientId));
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        return kafkaConsumer;
    }
    
    class KeepAliveKafkaThread implements Runnable {

        @Override
        public void run() {
            while (alwaysWithDelay(1000)) {

                for (Map.Entry<String, Future<?>> entry : futuresMap.entrySet()) {
                    if (entry.getValue().isDone()) {
                        LOGGER.info("El consumidor " + entry.getKey() + " se ha desconectado. Planificando otro consumidor para el mismo Topic...");
                        try {
                            if (CONSUMER_JAXI_MESSAGES_1_NAME.equals(entry.getKey())) {
                                    futuresMap.put(CONSUMER_JAXI_MESSAGES_1_NAME, startConsumerForJaxiTopic(ApplicationContextProvider.getApplicationContext()));
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
