package ch.unisg.monitoring.kafka.serialization.json.vgr;

import ch.unisg.monitoring.domain.stations.VGR_1;
import ch.unisg.monitoring.kafka.serialization.InstantTypeAdapter;
import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.Instant;

public class VgrDeserializer implements JsonDeserializer<VGR_1> {
    private static final Gson gsonVGR = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
            .create();

    @Override
    public VGR_1 deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return gsonVGR.fromJson(json.getAsJsonObject(), VGR_1.class);
    }
}