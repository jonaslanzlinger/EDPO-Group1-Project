package ch.unisg.serialization.json.hbw;

import ch.unisg.serialization.HbwEvent;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

/**
 * This class is a Serde for the HbwEvent class, returning the corresponding serializer and deserializer
 */
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
