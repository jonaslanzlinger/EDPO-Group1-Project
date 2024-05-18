package ch.unisg.monitoring.serialization.json;


import ch.unisg.monitoring.serialization.FactoryEvent;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

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
