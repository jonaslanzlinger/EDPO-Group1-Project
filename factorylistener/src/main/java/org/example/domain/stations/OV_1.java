package org.example.domain.stations;

public class OV_1 extends Station {

    private String timestamp;
    private boolean i1_pos_switch;
    private boolean i2_pos_switch;
    private boolean i5_light_barrier;
    private double m1_speed;
    private boolean o7_valve;
    private double o8_compressor;
    private String current_state;
    private String current_task;
    private double current_task_duration;
    private String current_sub_task;
    private String failure_label;

    public OV_1(String id, String station, String timestamp, boolean i1_pos_switch, boolean i2_pos_switch, boolean i5_light_barrier, double m1_speed, boolean o7_valve, double o8_compressor, String current_state, String current_task, double current_task_duration, String current_sub_task, String failure_label) {
        super(id, station);
        this.timestamp = timestamp;
        this.i1_pos_switch = i1_pos_switch;
        this.i2_pos_switch = i2_pos_switch;
        this.i5_light_barrier = i5_light_barrier;
        this.m1_speed = m1_speed;
        this.o7_valve = o7_valve;
        this.o8_compressor = o8_compressor;
        this.current_state = current_state;
        this.current_task = current_task;
        this.current_task_duration = current_task_duration;
        this.current_sub_task = current_sub_task;
        this.failure_label = failure_label;
    }


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isI1_pos_switch() {
        return i1_pos_switch;
    }

    public void setI1_pos_switch(boolean i1_pos_switch) {
        this.i1_pos_switch = i1_pos_switch;
    }

    public boolean isI2_pos_switch() {
        return i2_pos_switch;
    }

    public void setI2_pos_switch(boolean i2_pos_switch) {
        this.i2_pos_switch = i2_pos_switch;
    }

    public boolean isI5_light_barrier() {
        return i5_light_barrier;
    }

    public void setI5_light_barrier(boolean i5_light_barrier) {
        this.i5_light_barrier = i5_light_barrier;
    }

    public double getM1_speed() {
        return m1_speed;
    }

    public void setM1_speed(double m1_speed) {
        this.m1_speed = m1_speed;
    }

    public boolean isO7_valve() {
        return o7_valve;
    }

    public void setO7_valve(boolean o7_valve) {
        this.o7_valve = o7_valve;
    }

    public double getO8_compressor() {
        return o8_compressor;
    }

    public void setO8_compressor(double o8_compressor) {
        this.o8_compressor = o8_compressor;
    }

    public String getCurrent_state() {
        return current_state;
    }

    public void setCurrent_state(String current_state) {
        this.current_state = current_state;
    }

    public String getCurrent_task() {
        return current_task;
    }

    public void setCurrent_task(String current_task) {
        this.current_task = current_task;
    }

    public double getCurrent_task_duration() {
        return current_task_duration;
    }

    public void setCurrent_task_duration(double current_task_duration) {
        this.current_task_duration = current_task_duration;
    }

    public String getCurrent_sub_task() {
        return current_sub_task;
    }

    public void setCurrent_sub_task(String current_sub_task) {
        this.current_sub_task = current_sub_task;
    }

    public String getFailure_label() {
        return failure_label;
    }

    public void setFailure_label(String failure_label) {
        this.failure_label = failure_label;
    }
}