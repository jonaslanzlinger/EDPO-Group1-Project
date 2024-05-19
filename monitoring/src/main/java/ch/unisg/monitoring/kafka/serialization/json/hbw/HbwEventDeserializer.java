package ch.unisg.monitoring.kafka.serialization.json.hbw;


import ch.unisg.monitoring.domain.stations.HBW_1;
import ch.unisg.monitoring.kafka.serialization.HbwEvent;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

public class HbwEventDeserializer implements Deserializer<HbwEvent> {

    private static final Gson gsonHBW = new GsonBuilder()
            .registerTypeAdapter(HBW_1.class, new HbwDeserializer())
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
  
    public HbwEvent deserialize(String topic, byte[] bytes) {
        if (bytes == null) return null;
        return gsonHBW.fromJson(new String(bytes, StandardCharsets.UTF_8), HbwEvent.class);
    }
}
