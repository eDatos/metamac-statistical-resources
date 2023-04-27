package org.siemac.metamac.statistical.resources.web.server.stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.common.record.TimestampType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class KafkaUtils {

    protected static Log LOGGER = LogFactory.getLog(KafkaUtils.class);

    public static boolean alwaysWithDelay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
        return true;
    }

    public static StringBuilder buildLogMessage(String message, String topicName, int partition, long offset, TimestampType timestampType, long timestamp) {
        StringBuilder logMessageBldr = new StringBuilder("Received message from Kafka -> Topic Name: ");
        logMessageBldr.append(topicName).append(", Partition: ").append(partition).append(", Offset: ").append(offset).append(", TimestampType: ").append(timestampType).append(", Timestamp: ")
                .append(timestamp).append(" [").append(new DateTime(timestamp, DateTimeZone.forID("Atlantic/Canary"))).append("]");

        return logMessageBldr;
    }
}
