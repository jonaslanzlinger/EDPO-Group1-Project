package ch.unisg.monitoring.kafka.serialization.json.hbw;

import ch.unisg.monitoring.kafka.serialization.HbwEvent;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class HbwEventSerdes implements Serde<HbwEvent> {

    @Override
    public Serializer<HbwEvent> serializer() {
        return new HbwEventSerializer();
    }

    @Override
    public Deserializer<HbwEvent> deserializer() {
        return new HbwEventDeserializer();
    }

}
