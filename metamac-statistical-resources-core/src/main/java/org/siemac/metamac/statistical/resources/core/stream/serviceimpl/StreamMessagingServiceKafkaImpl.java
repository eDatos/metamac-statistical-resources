package org.siemac.metamac.statistical.resources.core.stream.serviceimpl;

import java.util.Properties;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.HasSiemacMetadata;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConfigurationConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.stream.messages.QueryVersionAvro;
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

import es.ibestat.jaxi.stream.messages.DatasetAvro;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;

@Component(StreamMessagingService.BEAN_ID)
public class StreamMessagingServiceKafkaImpl<K, V extends SpecificRecordBase> implements StreamMessagingService<K, V>, ApplicationListener<ContextClosedEvent> {

    @Autowired
    private StatisticalResourcesConfiguration statisticalResourcesConfig;

    @Autowired
    private QueryVersionDo2AvroMapper         queryVersionDo2AvroMapper;

    private ProducerBase<K, V>                producer;

    private final String                      CONSUMER_QUERY_1_NAME = "statresources_producer_1";

    @Override
    public void sendMessage(HasSiemacMetadata message) throws MetamacException {
        // Serialize message
        MessageBase<K, V> m = new AvroMessage<K, V>(serializeKey(message), serializeMessage(message));

        // Topic
        String topic = getTopicByType(message);

        getProducer().sendMessage(m, topic);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sendMessage(QueryVersion message) throws MetamacException {
        // To Avro
        QueryVersionAvro queryVersionAvro = queryVersionDo2AvroMapper.queryVersionDoToAvro(message);
        K key = (K) message.getLifeCycleStatisticalResource().getUrn();

        // Serialize message
        MessageBase<K, V> m = new AvroMessage<K, V>(key, (V) queryVersionAvro);

        // Topic
        String topic = statisticalResourcesConfig.retrieveKafkaTopicQueryPublication();

        getProducer().sendMessage(m, topic);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sendMessage(DatasetAvro message) throws MetamacException { // TODO EDATOS-3770 QUITAR
        // To Avro
        K key = (K) message.getUrn();

        // Serialize message
        MessageBase<K, V> m = new AvroMessage<K, V>(key, (V) message);

        // Topic
        String topic = "JAXI_PUBLICATIONS";

        getProducer().sendMessage(m, topic);
    }
    
    private ProducerBase<K, V> getProducer() throws MetamacException {
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

    private String getTopicByType(HasSiemacMetadata version) throws MetamacException {
        switch (version.getSiemacMetadataStatisticalResource().getType()) {
            case DATASET:
                return statisticalResourcesConfig.retrieveKafkaTopicDatasetsPublication();
            case COLLECTION:
                return statisticalResourcesConfig.retrieveKafkaTopicCollectionPublication();
            case MULTIDATASET:
                return "multidatasetTopic"; // TODO METAMAC-2715 - Realizar la notificación a Kafka de los recursos Multidataset
            default:
                return null;
        }
    }

    @SuppressWarnings("unchecked")
    private V serializeMessage(HasSiemacMetadata version) throws MetamacException {
        switch (version.getSiemacMetadataStatisticalResource().getType()) {
            case DATASET:
                return (V) DatasetVersionDo2AvroMapper.do2Avro((DatasetVersion) version);
            case COLLECTION:
                return (V) PublicationVersionDo2AvroMapper.do2Avro((PublicationVersion) version);
            case MULTIDATASET:
                return null; // TODO METAMAC-2715 - Realizar la notificación a Kafka de los recursos Multidataset
            // Nótese que las queries NO se publican y por tanto no hace falta el case aquí
            default:
                return null;
        }
    }

    @SuppressWarnings("unchecked")
    private K serializeKey(HasSiemacMetadata version) throws MetamacException {
        return (K) version.getSiemacMetadataStatisticalResource().getUrn();
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        if (producer != null) {
            producer.close();
            producer = null;
        }
    }

}
