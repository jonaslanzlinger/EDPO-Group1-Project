package ch.unisg.monitoring.kafka.serialization.json.vgr;


import ch.unisg.monitoring.domain.stations.VGR_1;
import com.google.gson.*;

import java.lang.reflect.Type;

public class VgrDeserializer implements JsonDeserializer<VGR_1> {
    @Override
    public VGR_1 deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new Gson().fromJson(json.getAsJsonObject(), VGR_1.class);
    }
}