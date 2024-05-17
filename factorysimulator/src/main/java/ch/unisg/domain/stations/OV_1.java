package ch.unisg.domain.stations;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class OV_1 extends Station {


    private String timestamp;
    private double i1_pos_switch;
    private double i2_pos_switch;
    private double i5_light_barrier;
    private double m1_speed;
    private double o7_valve;
    private double o8_compressor;
    private String current_state;
    private String current_task;
    private double current_task_duration;
    private String current_sub_task;
    private String failure_label;

}