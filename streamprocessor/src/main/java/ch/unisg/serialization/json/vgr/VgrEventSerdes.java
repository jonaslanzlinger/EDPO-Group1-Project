package ch.unisg.serialization.json.vgr;

import ch.unisg.serialization.FactoryEvent;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class VgrEventSerdes implements Serde<FactoryEvent> {

    @Override
    public Serializer<FactoryEvent> serializer() {
        return new VgrEventSerializer();
    }

    @Override
    public Deserializer<FactoryEvent> deserializer() {
        return new VgrEventDeserializer();
    }

}
