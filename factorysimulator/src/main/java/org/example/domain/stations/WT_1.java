package org.example.domain.stations;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WT_1 extends Station {

    private String timestamp;
    private boolean i3_pos_switch;
    private boolean i4_pos_switch;
    private double m2_speed;
    private boolean o5_valve;
    private boolean o6_valve;
    private double o8_compressor;
    private String current_state;
    private String current_task;
    private double current_task_duration;
    private String current_sub_task;
    private String failure_label;

    public WT_1(String id, String station, String timestamp, boolean i3_pos_switch, boolean i4_pos_switch, double m2_speed, boolean o5_valve, boolean o6_valve, double o8_compressor, String current_state, String current_task, double current_task_duration, String current_sub_task, String failure_label) {
        super(id, station);
        this.timestamp = timestamp;
        this.i3_pos_switch = i3_pos_switch;
        this.i4_pos_switch = i4_pos_switch;
        this.m2_speed = m2_speed;
        this.o5_valve = o5_valve;
        this.o6_valve = o6_valve;
        this.o8_compressor = o8_compressor;
        this.current_state = current_state;
        this.current_task = current_task;
        this.current_task_duration = current_task_duration;
        this.current_sub_task = current_sub_task;
        this.failure_label = failure_label;
    }
}