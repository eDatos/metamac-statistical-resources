package org.siemac.metamac.statistical.resources.web.server.stream;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;

public class KafkaCustomProducer<K, V extends SpecificRecordBase> implements ProducerBase<K, V> {

    private static final int KAFKA_TIMEOUT = 500;
    private final static String[] MANDATORY_SETTINGS = {KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, ProducerConfig.BOOTSTRAP_SERVERS_CONFIG};
    private final static Map<String, Object> DEFAULT_SETTINGS;
    private KafkaProducer<K, V> producer;

    static {
        DEFAULT_SETTINGS = new HashMap<String, Object>();
        DEFAULT_SETTINGS.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        DEFAULT_SETTINGS.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        DEFAULT_SETTINGS.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000); // Time to wait if Kafka cluster is down or buffer is full
        DEFAULT_SETTINGS.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 10485760); // 10 MB, note: The Broker has: message.max.bytes=10485760 (10MB)
    };

    public KafkaCustomProducer(Properties props) throws MetamacException {
        super();
        checkForMissingProperties(props);
        producer = new KafkaProducer<K, V>(props);
    }

    @Override
    public void sendMessage(MessageBase<K, V> message, String topic) throws MetamacException {
        ProducerRecord<K, V> record = new ProducerRecord<K, V>(topic, message.getKey(), message.getContent());
        try {
            Future<RecordMetadata> sendResult = producer.send(record);
            sendResult.get(KAFKA_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (SerializationException | InterruptedException | ExecutionException | TimeoutException e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.UNABLE_TO_SEND_STREAM_MESSAGING_TO_STREAM_MESSAGING_SERVER).withCause(e).build();
        }
    }

    private Properties checkForMissingProperties(Properties props) throws MetamacException {
        checkMissingMandatoryProperties(props);
        props = fillMissingDefaultProperties(props);
        return props;
    }

    private void checkMissingMandatoryProperties(Properties props) throws MetamacException {
        for (String prop : KafkaCustomProducer.MANDATORY_SETTINGS) {
            if (!props.containsKey(prop)) {
                throw new MetamacExceptionBuilder().withMessageParameters(ServiceExceptionType.STREAM_MESSAGING_MISSING_MANDATORY_SETTINGS.getMessageForReasonType()).build();
            }
        }
    }

    private Properties fillMissingDefaultProperties(Properties props) {
        for (Object key : DEFAULT_SETTINGS.keySet()) {
            if (!props.containsKey(key)) {
                props.put(key, DEFAULT_SETTINGS.get(key));
            }
        }
        return props;
    }

    @Override
    public void close() {
        if (null != producer) {
            producer.close(0, TimeUnit.MILLISECONDS);
        }
    }

}
