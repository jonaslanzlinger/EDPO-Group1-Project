package ch.unisg.serialization.json.vgr;

import ch.unisg.serialization.FactoryEvent;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

public class VgrEventDeserializer implements Deserializer<FactoryEvent> {
    private final Gson gson =
        new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();

    @Override
    public FactoryEvent deserialize(String topic, byte[] bytes) {
        if (bytes == null) return null;
        return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), FactoryEvent.class);
    }
}
