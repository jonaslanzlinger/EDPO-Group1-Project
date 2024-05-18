package ch.unisg.monitoring.serialization.json.vgr;

import ch.unisg.monitoring.serialization.VgrEvent;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class VgrEventSerdes implements Serde<VgrEvent> {

    @Override
    public Serializer<VgrEvent> serializer() {
        return new VgrEventSerializer();
    }

    @Override
    public Deserializer<VgrEvent> deserializer() {
        return new VgrEventDeserializer();
    }

}
