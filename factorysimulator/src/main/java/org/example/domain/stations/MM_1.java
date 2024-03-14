package org.example.domain.stations;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MM_1 extends Station {

    private String timestamp;
    private boolean i1_pos_switch;
    private boolean i2_pos_switch;
    private boolean i3_pos_switch;
    private boolean i4_light_barrier;
    private double m1_speed;
    private double m2_speed;
    private double m3_speed;
    private boolean o7_valve;
    private double o8_compressor;
    private String current_state;
    private String current_task;
    private double current_task_duration;
    private String current_sub_task;
    private String failure_label;

    public MM_1(String id, String station, String timestamp, boolean i1_pos_switch, boolean i2_pos_switch, boolean i3_pos_switch, boolean i4_light_barrier, double m1_speed, double m2_speed, double m3_speed, boolean o7_valve, double o8_compressor, String current_state, String current_task, double current_task_duration, String current_sub_task, String failure_label) {
        super(id, station);
        this.timestamp = timestamp;
        this.i1_pos_switch = i1_pos_switch;
        this.i2_pos_switch = i2_pos_switch;
        this.i3_pos_switch = i3_pos_switch;
        this.i4_light_barrier = i4_light_barrier;
        this.m1_speed = m1_speed;
        this.m2_speed = m2_speed;
        this.m3_speed = m3_speed;
        this.o7_valve = o7_valve;
        this.o8_compressor = o8_compressor;
        this.current_state = current_state;
        this.current_task = current_task;
        this.current_task_duration = current_task_duration;
        this.current_sub_task = current_sub_task;
        this.failure_label = failure_label;
    }
}