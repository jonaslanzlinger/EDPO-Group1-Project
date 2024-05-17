package ch.unisg.domain.stations;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class WT_1 extends Station {

    private String timestamp;
    private double i3_pos_switch;
    private double i4_pos_switch;
    private double m2_speed;
    private double o5_valve;
    private double o6_valve;
    private double o8_compressor;
    private String current_state;
    private String current_task;
    private double current_task_duration;
    private String current_sub_task;
    private String failure_label;
}