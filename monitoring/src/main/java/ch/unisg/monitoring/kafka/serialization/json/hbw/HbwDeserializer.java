package ch.unisg.monitoring.kafka.serialization.json.hbw;

import ch.unisg.monitoring.domain.stations.HBW_1;
import com.google.gson.*;
import java.lang.reflect.Type;

public class HbwDeserializer implements JsonDeserializer<HBW_1> {
    @Override
    public HBW_1 deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new Gson().fromJson(json.getAsJsonObject(), HBW_1.class);
    }
}
