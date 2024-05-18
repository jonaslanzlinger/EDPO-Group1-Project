package ch.unisg.monitoring.serialization.json.vgr;

import ch.unisg.monitoring.serialization.VgrEvent;
import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

public class VgrEventSerializer implements Serializer<VgrEvent> {

    private final Gson gson = new Gson();

    @Override
    public byte[] serialize(String topic, VgrEvent factoryEvent) {
        if (factoryEvent == null) return null;
        return gson.toJson(factoryEvent).getBytes(StandardCharsets.UTF_8);
    }

}
