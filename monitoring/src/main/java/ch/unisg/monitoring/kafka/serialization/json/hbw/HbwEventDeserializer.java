package ch.unisg.monitoring.kafka.serialization.json.hbw;

import ch.unisg.monitoring.domain.stations.HBW_1;

import ch.unisg.monitoring.kafka.serialization.HbwEvent;
import ch.unisg.monitoring.kafka.serialization.InstantTypeAdapter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * This is a deserializer class for the HBW_1 class.
 * Special TypeAdapters need to be registered for deserialization.
 */
public class HbwEventDeserializer implements Deserializer<HbwEvent> {

    private static final Gson gsonHBW = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
            .registerTypeAdapter(HBW_1.class, new HbwDeserializer())
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
  
    public HbwEvent deserialize(String topic, byte[] bytes) {
        if (bytes == null) return null;
        return gsonHBW.fromJson(new String(bytes, StandardCharsets.UTF_8), HbwEvent.class);
    }
}
