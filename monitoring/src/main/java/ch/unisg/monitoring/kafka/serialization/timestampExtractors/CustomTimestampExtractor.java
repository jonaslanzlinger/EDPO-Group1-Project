package ch.unisg.monitoring.kafka.serialization.timestampExtractors;

import ch.unisg.monitoring.kafka.serialization.HbwEvent;
import ch.unisg.monitoring.kafka.serialization.VgrEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

public class CustomTimestampExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long previousTimestamp) {
        Object event = record.value();

        if (event instanceof VgrEvent) {
            VgrEvent vgrEvent = (VgrEvent) event;
            return vgrEvent.getData().getTimestamp().toEpochMilli();
        } else if (event instanceof HbwEvent) {
            HbwEvent hbwEvent = (HbwEvent) event;
            return hbwEvent.getData().getTimestamp().toEpochMilli();
        } else {
            throw new IllegalArgumentException("Unknown event type: " + event.getClass());
        }
    }
}
