package ch.unisg.monitoring.kafka.timestampExtractors;

import ch.unisg.monitoring.kafka.serialization.FactoryEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

import java.time.Instant;

public class FactoryEventTimestampExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
        FactoryEvent factoryEvent = (FactoryEvent) record.value();
        Instant instant = Instant.parse(factoryEvent.getTime());
        return instant.toEpochMilli();
    }
}
