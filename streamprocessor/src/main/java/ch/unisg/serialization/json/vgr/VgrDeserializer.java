package ch.unisg.serialization.json.vgr;

import ch.unisg.domain.stations.VGR_1;
import com.google.gson.*;

import java.lang.reflect.Type;

public class VgrDeserializer implements JsonDeserializer<VGR_1> {
    @Override
    public VGR_1 deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        VGR_1 vgr = new VGR_1();

        vgr.setTimestamp(jsonObject.get("timestamp").getAsString());
        vgr.setI1_pos_switch(jsonObject.get("i1_pos_switch").getAsDouble() == 1.0);
        vgr.setI2_pos_switch(jsonObject.get("i2_pos_switch").getAsDouble() == 1.0);
        vgr.setI3_pos_switch(jsonObject.get("i3_pos_switch").getAsDouble() == 1.0);
        vgr.setI7_light_barrier(jsonObject.get("i7_light_barrier").getAsDouble() == 1.0);
        vgr.setI4_light_barrier(jsonObject.get("i4_light_barrier").getAsDouble() == 1.0);
        vgr.setI8_color_sensor(jsonObject.get("i8_color_sensor").getAsDouble());

        double colorReading = vgr.getI8_color_sensor();
        String color;
        if (colorReading > 1550) {
            color = "none";
        } else if (colorReading > 1500) {
            color = "blue";
        } else if(colorReading > 1000) {
            color = "red";
        } else {
            color = "white";
        }
        vgr.setColor(color);
        vgr.setO7_compressor_level(jsonObject.get("o7_compressor_level").getAsDouble());
        vgr.setO8_valve_open(jsonObject.get("o8_valve_open").getAsDouble() == 1.0);
        vgr.setM1_speed(jsonObject.get("m1_speed").getAsDouble());
        vgr.setM2_speed(jsonObject.get("m2_speed").getAsDouble());
        vgr.setM3_speed(jsonObject.get("m3_speed").getAsDouble());
        vgr.setCurrent_state(jsonObject.get("current_state").getAsString());
        vgr.setCurrent_task(jsonObject.get("current_task").getAsString());
        vgr.setCurrent_task_duration(jsonObject.get("current_task_duration").getAsDouble());
        vgr.setCurrent_sub_task(jsonObject.get("current_sub_task").getAsString());
        if (jsonObject.get("failure_label") != null) {
            vgr.setFailure_label(jsonObject.get("failure_label").getAsString());
        }
        vgr.setCurrent_pos_x(jsonObject.get("current_pos_x").getAsDouble());
        vgr.setCurrent_pos_y(jsonObject.get("current_pos_y").getAsDouble());
        vgr.setCurrent_pos_z(jsonObject.get("current_pos_z").getAsDouble());
        vgr.setTarget_pos_x(jsonObject.get("target_pos_x").getAsDouble());
        vgr.setTarget_pos_y(jsonObject.get("target_pos_y").getAsDouble());
        vgr.setTarget_pos_z(jsonObject.get("target_pos_z").getAsDouble());

        return vgr;
    }
}