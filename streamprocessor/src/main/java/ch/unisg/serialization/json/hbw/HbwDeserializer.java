package ch.unisg.serialization.json.hbw;

import ch.unisg.domain.stations.HBW_1;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a deserializer class for the HBW_1 class.
 */
public class HbwDeserializer implements JsonDeserializer<HBW_1> {
    @Override
    public HBW_1 deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        HBW_1 hbw = new HBW_1();

        hbw.setId(jsonObject.get("id").getAsString());
        hbw.setStation(jsonObject.get("station").getAsString());

        String timeStamp = jsonObject.get("timestamp").getAsString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
        LocalDateTime dateTime = LocalDateTime.parse(timeStamp, formatter);
        Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();

        hbw.setTimestamp(instant);
        hbw.setI1_light_barrier(jsonObject.get("i1_light_barrier").getAsDouble() == 1.0);
        hbw.setI2_light_barrier(jsonObject.get("i2_light_barrier").getAsDouble() == 1.0);
        hbw.setI3_light_barrier(jsonObject.get("i3_light_barrier").getAsDouble() == 1.0);
        hbw.setI4_light_barrier(jsonObject.get("i4_light_barrier").getAsDouble() == 1.0);
        hbw.setI5_pos_switch(jsonObject.get("i5_pos_switch").getAsDouble() == 1.0);
        hbw.setI6_pos_switch(jsonObject.get("i6_pos_switch").getAsDouble() == 1.0);
        hbw.setI7_pos_switch(jsonObject.get("i7_pos_switch").getAsDouble() == 1.0);
        hbw.setI8_pos_switch(jsonObject.get("i8_pos_switch").getAsDouble() == 1.0);
        hbw.setM1_speed(jsonObject.get("m1_speed").getAsDouble());
        hbw.setM2_speed(jsonObject.get("m2_speed").getAsDouble());
        hbw.setM3_speed(jsonObject.get("m3_speed").getAsDouble());
        hbw.setM4_speed(jsonObject.get("m4_speed").getAsDouble());
        hbw.setCurrent_state(jsonObject.get("current_state").getAsString());
        hbw.setCurrent_task(jsonObject.get("current_task").getAsString());
        hbw.setCurrent_task_duration(jsonObject.get("current_task_duration").getAsDouble());
        hbw.setCurrent_sub_task(jsonObject.get("current_sub_task").getAsString());
        if (jsonObject.get("failure_label") != null) {
            hbw.setFailure_label(jsonObject.get("failure_label").getAsString());
        }
        hbw.setCurrent_pos_x(jsonObject.get("current_pos_x").getAsDouble());
        hbw.setCurrent_pos_y(jsonObject.get("current_pos_y").getAsDouble());
        hbw.setTarget_pos_x(jsonObject.get("target_pos_x").getAsDouble());
        hbw.setTarget_pos_y(jsonObject.get("target_pos_y").getAsDouble());
        hbw.setAmount_of_workpieces(jsonObject.get("amount_of_workpieces").getAsInt());

        // Assuming the current_stock is a map of String to String
        JsonObject currentStockJsonObject = jsonObject.get("current_stock").getAsJsonObject();
        Map<String, String> currentStockMap = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : currentStockJsonObject.entrySet()) {
            currentStockMap.put(entry.getKey(), entry.getValue().getAsString());
        }
        hbw.setCurrent_stock(currentStockMap);

        return hbw;
    }
}
