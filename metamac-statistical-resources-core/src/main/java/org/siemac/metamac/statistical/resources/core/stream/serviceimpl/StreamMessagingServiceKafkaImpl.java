package org.siemac.metamac.statistical.resources.core.stream.serviceimpl;

import java.util.Properties;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.statistical.resources.core.base.domain.HasLifecycle;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConfigurationConstants;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionRepository;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical.resources.core.stream.messages.mappers.DatasetVersionDo2AvroMapper;
import org.siemac.metamac.statistical.resources.core.stream.messages.mappers.PublicationVersionDo2AvroMapper;
import org.siemac.metamac.statistical.resources.core.stream.messages.mappers.QueryVersionDo2AvroMapper;
import org.siemac.metamac.statistical.resources.core.stream.serviceapi.StreamMessagingService;
import org.siemac.metamac.statistical.resources.web.server.stream.AvroMessage;
import org.siemac.metamac.statistical.resources.web.server.stream.KafkaCustomProducer;
import org.siemac.metamac.statistical.resources.web.server.stream.MessageBase;
import org.siemac.metamac.statistical.resources.web.server.stream.ProducerBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;

@Component(StreamMessagingService.BEAN_ID)
public class StreamMessagingServiceKafkaImpl implements StreamMessagingService<String, SpecificRecordBase>, ApplicationListener<ContextClosedEvent> {

    @Autowired
    private StatisticalResourcesConfiguration statisticalResourcesConfig;

    @Autowired
    private DatasetVersionRepository datasetVersionRepository;

    @Autowired
    private PublicationVersionRepository publicationVersionRepository;

    @Autowired
    private QueryVersionRepository queryVersionRepository;

    @Autowired
    private QueryVersionDo2AvroMapper queryVersionDo2AvroMapper;

    private ProducerBase<String, SpecificRecordBase> producer;

    private final String CONSUMER_QUERY_1_NAME = "statresources_producer_1";

    @Override
    public void sendMessage(HasLifecycle message) throws MetamacException {
        MessageBase<String, SpecificRecordBase> m = new AvroMessage<>(serializeKey(message), serializeMessage(message));
        String topic = getTopicByType(message);
        getProducer().sendMessage(m, topic);
    }

    private ProducerBase<String, SpecificRecordBase> getProducer() throws MetamacException {
        if (producer == null) {
            producer = new KafkaCustomProducer<>(getProducerProperties());
        }
        return producer;
    }

    private Properties getProducerProperties() throws MetamacException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, statisticalResourcesConfig.retrieveProperty(StatisticalResourcesConfigurationConstants.KAFKA_BOOTSTRAP_SERVERS));
        props.put(ProducerConfig.CLIENT_ID_CONFIG, CONSUMER_QUERY_1_NAME);

        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        props.put(ProducerConfig.RETRIES_CONFIG, 10);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);

        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, statisticalResourcesConfig.retrieveProperty(StatisticalResourcesConfigurationConstants.KAFKA_SCHEMA_REGISTRY_URL));
        return props;
    }

    private String getTopicByType(HasLifecycle version) throws MetamacException {
        switch (version.getLifeCycleStatisticalResource().getType()) {
            case DATASET:
                return statisticalResourcesConfig.retrieveKafkaTopicDatasetsPublication();
            case QUERY:
                return statisticalResourcesConfig.retrieveKafkaTopicQueryPublication();
            case COLLECTION:
                return statisticalResourcesConfig.retrieveKafkaTopicCollectionPublication();
            case MULTIDATASET:
                return "multidatasetTopic"; // TODO METAMAC-2715 - Realizar la notificación a Kafka de los recursos Multidataset
            default:
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.STREAM_MESSAGING_TOPIC_IS_INVALID)
                        .withMessageParameters(version.getLifeCycleStatisticalResource().getType()).build();
        }
    }

    private SpecificRecordBase serializeMessage(HasLifecycle version) throws MetamacException {
        String urlBaseExternalVisualizer = statisticalResourcesConfig.retrievePortalExternalWebApplicationUrlVisualizer();
        String urn = version.getLifeCycleStatisticalResource().getUrn();
        switch (version.getLifeCycleStatisticalResource().getType()) {
            case DATASET:
                DatasetVersion datasetVersion = version instanceof DatasetVersion ? (DatasetVersion) version : datasetVersionRepository.retrieveByUrn(urn);
                return DatasetVersionDo2AvroMapper.do2Avro(datasetVersion, urlBaseExternalVisualizer);
            case QUERY:
                QueryVersion queryVersion = version instanceof QueryVersion ? (QueryVersion) version : queryVersionRepository.retrieveByUrn(urn);
                return queryVersionDo2AvroMapper.queryVersionDoToAvro(queryVersion);
            case COLLECTION:
                PublicationVersion publicationVersion = version instanceof PublicationVersion ? (PublicationVersion) version : publicationVersionRepository.retrieveByUrn(urn);
                return PublicationVersionDo2AvroMapper.do2Avro(publicationVersion);
            case MULTIDATASET:
                return null; // TODO METAMAC-2715 - Realizar la notificación a Kafka de los recursos Multidataset
            default:
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.STREAM_MESSAGING_TOPIC_IS_INVALID)
                        .withMessageParameters(version.getLifeCycleStatisticalResource().getType()).build();
        }
    }

    private String serializeKey(HasLifecycle version) {
        return version.getLifeCycleStatisticalResource().getUrn();
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        if (producer != null) {
            producer.close();
            producer = null;
        }
    }

}
