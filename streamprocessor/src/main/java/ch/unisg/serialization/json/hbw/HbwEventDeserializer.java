package ch.unisg.serialization.json.hbw;

import ch.unisg.domain.stations.HBW_1;
import ch.unisg.serialization.HbwEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

/**
 * This is a deserializer class for the HBW_1 class.
 * Special TypeAdapters need to be registered for deserialization.
 */
public class HbwEventDeserializer implements Deserializer<HbwEvent> {

    private static final Gson gsonHBW = new GsonBuilder()
            .registerTypeAdapter(HBW_1.class, new HbwDeserializer())
            .create();

    @Override
    public HbwEvent deserialize(String topic, byte[] bytes) {
        if (bytes == null) return null;
        return gsonHBW.fromJson(new String(bytes, StandardCharsets.UTF_8), HbwEvent.class);
    }
}
