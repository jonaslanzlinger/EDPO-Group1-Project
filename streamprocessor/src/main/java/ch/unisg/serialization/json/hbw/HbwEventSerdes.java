package ch.unisg.serialization.json.hbw;

import ch.unisg.serialization.FactoryEvent;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class HbwEventSerdes implements Serde<FactoryEvent> {

    @Override
    public Serializer<FactoryEvent> serializer() {
        return new HbwEventSerializer();
    }

    @Override
    public Deserializer<FactoryEvent> deserializer() {
        return new HbwEventDeserializer();
    }

}
