package org.example.domain.stations;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SM_1 extends Station {

    private String timestamp;
    private boolean i1_light_barrier;
    private int i2_color_sensor;
    private boolean i3_light_barrier;
    private boolean i6_light_barrier;
    private boolean i7_light_barrier;
    private boolean i8_light_barrier;
    private double m1_speed;
    private boolean o5_valve;
    private boolean o6_valve;
    private boolean o7_valve;
    private double o8_compressor;
    private String current_state;
    private String current_task;
    private double current_task_duration;
    private String current_sub_task;
    private String failure_label;

    public SM_1(String id, String station, String timestamp, boolean i1_light_barrier, int i2_color_sensor, boolean i3_light_barrier, boolean i6_light_barrier, boolean i7_light_barrier, boolean i8_light_barrier, double m1_speed, boolean o5_valve, boolean o6_valve, boolean o7_valve, double o8_compressor, String current_state, String current_task, double current_task_duration, String current_sub_task, String failure_label) {
        super(id, station);
        this.timestamp = timestamp;
        this.i1_light_barrier = i1_light_barrier;
        this.i2_color_sensor = i2_color_sensor;
        this.i3_light_barrier = i3_light_barrier;
        this.i6_light_barrier = i6_light_barrier;
        this.i7_light_barrier = i7_light_barrier;
        this.i8_light_barrier = i8_light_barrier;
        this.m1_speed = m1_speed;
        this.o5_valve = o5_valve;
        this.o6_valve = o6_valve;
        this.o7_valve = o7_valve;
        this.o8_compressor = o8_compressor;
        this.current_state = current_state;
        this.current_task = current_task;
        this.current_task_duration = current_task_duration;
        this.current_sub_task = current_sub_task;
        this.failure_label = failure_label;
    }
}