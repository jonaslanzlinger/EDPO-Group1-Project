package ch.unisg.serialization.json.hbw;

import ch.unisg.serialization.FactoryEvent;
import ch.unisg.serialization.HbwEvent;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

public class HbwEventDeserializer implements Deserializer<HbwEvent> {
    private final Gson gson =
        new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();

    @Override
    public HbwEvent deserialize(String topic, byte[] bytes) {
        if (bytes == null) return null;
        return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), HbwEvent.class);
    }
}
