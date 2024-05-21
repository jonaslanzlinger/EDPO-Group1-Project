package ch.unisg.serialization.json.vgr;

import ch.unisg.serialization.InstantTypeAdapter;
import ch.unisg.serialization.VgrEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;

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
