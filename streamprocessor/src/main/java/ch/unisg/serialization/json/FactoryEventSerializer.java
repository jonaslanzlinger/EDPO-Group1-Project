package ch.unisg.serialization.json;

import ch.unisg.serialization.FactoryEvent;
import ch.unisg.serialization.InstantTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * This class is responsible for serializing FactoryEvent objects into JSON strings.
 * Special TypeAdapters need to be registered.
 */
public class FactoryEventSerializer implements Serializer<FactoryEvent> {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
            .create();

    @Override
    public byte[] serialize(String topic, FactoryEvent factoryEvent) {
        if (factoryEvent == null) return null;
        return gson.toJson(factoryEvent).getBytes(StandardCharsets.UTF_8);
    }
}