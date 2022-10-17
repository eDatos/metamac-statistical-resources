package org.siemac.metamac.statistical.resources.web.server.stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.errors.TopicExistsException;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class KafkaInitializeTopics implements ApplicationListener<ContextRefreshedEvent> {

    protected static final Log               LOGGER             = LogFactory.getLog(KafkaInitializeTopics.class);

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

    @Autowired
    private StatisticalResourcesConfiguration statisticalResourcesConfiguration;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext ac = event.getApplicationContext();
        if (ac.getParent() == null) {
            try {
                propagateCreationOfTopics();
            } catch (Exception e) {
                LOGGER.error(e, e.getCause());
            }
        }
    }

    private void propagateCreationOfTopics() throws MetamacException {
        Properties kafkaProperties = getKafkaProperties();

        List<NewTopic> topics = getTopics();

        CreateTopicsOptions topicsOptions = getTopicsOptions();

        createTopics(kafkaProperties, topics, topicsOptions);
    }

    private Properties getKafkaProperties() throws MetamacException {
        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, statisticalResourcesConfiguration.retrieveKafkaBootStrapServers());

        return properties;
    }

    private List<NewTopic> getTopics() throws MetamacException {
        List<NewTopic> topics = new ArrayList<>();

        topics.add(createTopic(statisticalResourcesConfiguration.retrieveKafkaTopicQueryPublication()));
        topics.add(createTopic(statisticalResourcesConfiguration.retrieveKafkaTopicDatasetsPublication()));
        topics.add(createTopic(statisticalResourcesConfiguration.retrieveKafkaTopicCollectionPublication()));

        return topics;
    }

    private NewTopic createTopic(String topic) {
        return new NewTopic(topic, NUM_OF_PARTITIONS, NUM_OF_REPLICATION).configs(TOPIC_DEFAULT_SETTINGS);
    }

    private CreateTopicsOptions getTopicsOptions() {
        return new CreateTopicsOptions().timeoutMs(TIMEOUT);
    }

    private void createTopics(Properties kafkaProperties, List<NewTopic> topics, CreateTopicsOptions topicsOptions) {
        try (AdminClient adminClient = AdminClient.create(kafkaProperties)) {
            adminClient.createTopics(topics, topicsOptions).all().get();
        } catch (InterruptedException | ExecutionException e) {
            // Ignore if TopicExistsException, which may be valid if topic exists
            if (!(e.getCause() instanceof TopicExistsException)) {
                throw new RuntimeException("Imposible to create/check Topic in kafka", e);
            } else {
                LOGGER.info("Kafka topics already exist, it's not necessary to create them. The application deploy continues in the right way...");
            }
        }
    }
}
