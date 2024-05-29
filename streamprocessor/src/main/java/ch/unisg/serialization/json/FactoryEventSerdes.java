package ch.unisg.serialization.json;

import ch.unisg.serialization.FactoryEvent;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

/**
 * This class is used to serialize and deserialize FactoryEvent objects to and from JSON.
 */
public class FactoryEventSerdes implements Serde<FactoryEvent> {

    @Override
    public Serializer<FactoryEvent> serializer() {
        return new FactoryEventSerializer();
    }

    @Override
    public Deserializer<FactoryEvent> deserializer() {
        return new FactoryEventDeserializer();
    }
}