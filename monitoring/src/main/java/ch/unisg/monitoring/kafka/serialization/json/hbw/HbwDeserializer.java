package ch.unisg.monitoring.kafka.serialization.json.hbw;

import ch.unisg.monitoring.domain.stations.HBW_1;
import ch.unisg.monitoring.kafka.serialization.InstantTypeAdapter;
import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.Instant;

public class HbwDeserializer implements JsonDeserializer<HBW_1> {
    private static final Gson gsonHBW = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
            .create();

    @Override
    public HBW_1 deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return gsonHBW.fromJson(json.getAsJsonObject(), HBW_1.class);
    }
}
