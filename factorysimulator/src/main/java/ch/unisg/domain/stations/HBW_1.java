package ch.unisg.domain.stations;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Data
public class HBW_1 extends Station {

    private String timestamp;
    private double i1_light_barrier;
    private double i2_light_barrier;
    private double i3_light_barrier;
    private double i4_light_barrier;
    private double i5_pos_switch;
    private double i6_pos_switch;
    private double i7_pos_switch;
    private double i8_pos_switch;
    private double m1_speed;
    private double m2_speed;
    private double m3_speed;
    private double m4_speed;
    private String current_state;
    private String current_task;
    private double current_task_duration;
    private String current_sub_task;
    private String failure_label;
    private double current_pos_x;
    private double current_pos_y;
    private double target_pos_x;
    private double target_pos_y;
    private int amount_of_workpieces;
    private Map<String, String> current_stock;
}