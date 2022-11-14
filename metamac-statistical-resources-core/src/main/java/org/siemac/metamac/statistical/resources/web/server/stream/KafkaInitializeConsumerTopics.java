package org.siemac.metamac.statistical.resources.web.server.stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.errors.TopicExistsException;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;


public class KafkaInitializeConsumerTopics {

    protected static final Log               LOGGER             = LogFactory.getLog(KafkaInitializeConsumerTopics.class);

    // Create Topics Request
    private static final int                 NUM_OF_PARTITIONS  = 1;
    private static final short               NUM_OF_REPLICATION = (short) 1;
    private static final int                 TIMEOUT            = 1000;

    private static final String              RETENTION_MS       = "retention.ms";

    private static final Map<String, String> TOPIC_DEFAULT_SETTINGS;

    static {
        TOPIC_DEFAULT_SETTINGS = new HashMap<>();
        TOPIC_DEFAULT_SETTINGS.put(RETENTION_MS, "-1");
    };


    public static List<NewTopic> propagateCreationOfTopics(StatisticalResourcesConfiguration statisticalResourcesConfiguration) throws MetamacException {
        List<NewTopic> availableTopics = new ArrayList<>();
        Properties kafkaProperties = getKafkaProperties(statisticalResourcesConfiguration);

        List<NewTopic> topics = getTopics(statisticalResourcesConfiguration);

        if (!topics.isEmpty()) {
            CreateTopicsOptions topicsOptions = getTopicsOptions();

            availableTopics = createTopics(kafkaProperties, topics, topicsOptions);
        }
        return availableTopics;
    }

    private static Properties getKafkaProperties(StatisticalResourcesConfiguration statisticalResourcesConfiguration) throws MetamacException {
        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, statisticalResourcesConfiguration.retrieveKafkaBootStrapServers());

        return properties;
    }

    private static List<NewTopic> getTopics(StatisticalResourcesConfiguration statisticalResourcesConfiguration) throws MetamacException {
        List<NewTopic> topics = new ArrayList<>();

        try {
            // The topic can not be enabled in some environments.
            String topicNameDatasetExternalPublication = statisticalResourcesConfiguration.retrieveKafkaTopicExternalDatasetPublication();
            topics.add(createTopic(topicNameDatasetExternalPublication));
        } catch (Exception e) {
            LOGGER.info("retrieveKafkaTopicExternalDatasetPublication not found. Check if must exists in common metadata");
        }

        return topics;
    }

    private static NewTopic createTopic(String topic) {
        return new NewTopic(topic, NUM_OF_PARTITIONS, NUM_OF_REPLICATION).configs(TOPIC_DEFAULT_SETTINGS);
    }

    private static CreateTopicsOptions getTopicsOptions() {
        return new CreateTopicsOptions().timeoutMs(TIMEOUT);
    }

    private static List<NewTopic> getAvailableTopics(AdminClient adminClient, List<NewTopic> topics) {
        // excludes nonexistent topics
        List<NewTopic> availableTopics = new ArrayList<>();
        try {
        ListTopicsResult listTopics = adminClient.listTopics();
        Set<String> names = listTopics.names().get();
        for (NewTopic topic : topics) {
           if (names.contains(topic.name())) {
               availableTopics.add(topic);
           }
        }
        } catch(Exception e) {
            LOGGER.info("error to get list available topics");
        }
        return availableTopics;
    }
    
    private static List<NewTopic> createTopics(Properties kafkaProperties, List<NewTopic> topics, CreateTopicsOptions topicsOptions) {
        List<NewTopic> availableTopics = new ArrayList<>();
        try (AdminClient adminClient = AdminClient.create(kafkaProperties)) {

            availableTopics = getAvailableTopics(adminClient, topics);
            adminClient.createTopics(availableTopics, topicsOptions).all().get();
        } catch (InterruptedException | ExecutionException e) {
            // Ignore if TopicExistsException, which may be valid if topic exists
            if (!(e.getCause() instanceof TopicExistsException)) {
                throw new RuntimeException("Imposible to create/check Topic in kafka", e);
            } else {
                LOGGER.info("Kafka topics already exist, it's not necessary to create them. The application deploy continues in the right way...");
            }
        }
        return availableTopics;
    }
}
