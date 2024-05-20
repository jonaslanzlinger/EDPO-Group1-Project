package ch.unisg.monitoring.kafka.serialization.json.vgr;

import ch.unisg.monitoring.kafka.serialization.InstantTypeAdapter;
import ch.unisg.monitoring.kafka.serialization.VgrEvent;
import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Serializer;
import com.google.gson.GsonBuilder;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class VgrEventSerializer implements Serializer<VgrEvent> {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
            .create();

    @Override
    public byte[] serialize(String topic, VgrEvent factoryEvent) {
        if (factoryEvent == null) return null;
        return gson.toJson(factoryEvent).getBytes(StandardCharsets.UTF_8);
    }

}
