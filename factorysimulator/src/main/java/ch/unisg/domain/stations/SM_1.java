package ch.unisg.domain.stations;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class SM_1 extends Station {

    private String timestamp;
    private double i1_light_barrier;
    private int i2_color_sensor;
    private double i3_light_barrier;
    private double i6_light_barrier;
    private double i7_light_barrier;
    private double i8_light_barrier;
    private double m1_speed;
    private double o5_valve;
    private double o6_valve;
    private double o7_valve;
    private double o8_compressor;
    private String current_state;
    private String current_task;
    private double current_task_duration;
    private String current_sub_task;
    private String failure_label;

}