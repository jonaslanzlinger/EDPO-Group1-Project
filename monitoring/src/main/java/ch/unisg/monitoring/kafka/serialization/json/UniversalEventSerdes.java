package ch.unisg.monitoring.kafka.serialization.json;

import ch.unisg.monitoring.kafka.serialization.json.hbw.HbwEventDeserializer;
import ch.unisg.monitoring.kafka.serialization.json.vgr.VgrEventDeserializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;


public class UniversalEventSerdes implements Serde<Object> {

    private final VgrEventDeserializer vgrEventDeserializer;
    private final HbwEventDeserializer hbwEventDeserializer;

    public UniversalEventSerdes() {
        this.vgrEventDeserializer = new VgrEventDeserializer();
        this.hbwEventDeserializer = new HbwEventDeserializer();
    }

    @Override
    public Serializer<Object> serializer() {
        return null;
    }

    @Override
    public Deserializer<Object> deserializer() {
        return (topic, data) -> {
            if (data == null) {
                return null;
            }

            String json = new String(data);
            if (json.contains("VGR_1")) {
                return vgrEventDeserializer.deserialize(topic, data);
            } else if (json.contains("HBW_1")) {
                return hbwEventDeserializer.deserialize(topic, data);
            } else {
                throw new IllegalArgumentException("Unknown event type: " + json);
            }
        };
    }
}