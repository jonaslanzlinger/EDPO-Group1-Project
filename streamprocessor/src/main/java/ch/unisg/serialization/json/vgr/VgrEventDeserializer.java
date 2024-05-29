package ch.unisg.serialization.json.vgr;

import ch.unisg.domain.stations.VGR_1;
import ch.unisg.serialization.VgrEvent;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

/**
 * Deserializer for VGR_1 objects.
 * Special deserializer is needed because the VGR_1 class has a custom deserializer for Instant objects.
 */
public class VgrEventDeserializer implements Deserializer<VgrEvent> {

    private static final Gson gsonVGR = new GsonBuilder()
            .registerTypeAdapter(VGR_1.class, new VgrDeserializer())
            .create();

    @Override
    public VgrEvent deserialize(String topic, byte[] bytes) {
        if (bytes == null) return null;
        return gsonVGR.fromJson(new String(bytes, StandardCharsets.UTF_8), VgrEvent.class);
    }
}
