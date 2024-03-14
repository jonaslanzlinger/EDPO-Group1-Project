package org.example.domain.stations;

public class VGR_1 extends Station {

    private String timestamp;

    private boolean i1_pos_switch;

    private boolean i2_pos_switch;

    private boolean i3_pos_switch;

    private boolean i7_light_barrier;

    private boolean i4_light_barrier;

    private double i8_color_sensor;

    private double o7_compressor_level;

    private boolean o8_valve_open;

    private double m1_speed;

    private double m2_speed;

    private double m3_speed;

    private String current_state;

    private String current_task;

    private double current_task_duration;

    private String current_sub_task;

    private String failure_label;

    private double current_pos_x;

    private double current_pos_y;

    private double current_pos_z;

    private double target_pos_x;

    private double target_pos_y;

    private double target_pos_z;


    public VGR_1(String id, String station, String timestamp, boolean i1_pos_switch, boolean i2_pos_switch, boolean i3_pos_switch, boolean i7_light_barrier, boolean i4_light_barrier, double i8_color_sensor, double o7_compressor_level, boolean o8_valve_open, double m1_speed, double m2_speed, double m3_speed, String current_state, String current_task, double current_task_duration, String current_sub_task, String failure_label, double current_pos_x, double current_pos_y, double current_pos_z, double target_pos_x, double target_pos_y, double target_pos_z) {
        super(id, station);
        this.timestamp = timestamp;
        this.i1_pos_switch = i1_pos_switch;
        this.i2_pos_switch = i2_pos_switch;
        this.i3_pos_switch = i3_pos_switch;
        this.i7_light_barrier = i7_light_barrier;
        this.i4_light_barrier = i4_light_barrier;
        this.i8_color_sensor = i8_color_sensor;
        this.o7_compressor_level = o7_compressor_level;
        this.o8_valve_open = o8_valve_open;
        this.m1_speed = m1_speed;
        this.m2_speed = m2_speed;
        this.m3_speed = m3_speed;
        this.current_state = current_state;
        this.current_task = current_task;
        this.current_task_duration = current_task_duration;
        this.current_sub_task = current_sub_task;
        this.failure_label = failure_label;
        this.current_pos_x = current_pos_x;
        this.current_pos_y = current_pos_y;
        this.current_pos_z = current_pos_z;
        this.target_pos_x = target_pos_x;
        this.target_pos_y = target_pos_y;
        this.target_pos_z = target_pos_z;
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

    public boolean isI3_pos_switch() {
        return i3_pos_switch;
    }

    public void setI3_pos_switch(boolean i3_pos_switch) {
        this.i3_pos_switch = i3_pos_switch;
    }

    public boolean isI7_light_barrier() {
        return i7_light_barrier;
    }

    public void setI7_light_barrier(boolean i7_light_barrier) {
        this.i7_light_barrier = i7_light_barrier;
    }

    public boolean isI4_light_barrier() {
        return i4_light_barrier;
    }

    public void setI4_light_barrier(boolean i4_light_barrier) {
        this.i4_light_barrier = i4_light_barrier;
    }

    public double getI8_color_sensor() {
        return i8_color_sensor;
    }

    public void setI8_color_sensor(double i8_color_sensor) {
        this.i8_color_sensor = i8_color_sensor;
    }

    public double getO7_compressor_level() {
        return o7_compressor_level;
    }

    public void setO7_compressor_level(double o7_compressor_level) {
        this.o7_compressor_level = o7_compressor_level;
    }

    public boolean isO8_valve_open() {
        return o8_valve_open;
    }

    public void setO8_valve_open(boolean o8_valve_open) {
        this.o8_valve_open = o8_valve_open;
    }

    public double getM1_speed() {
        return m1_speed;
    }

    public void setM1_speed(double m1_speed) {
        this.m1_speed = m1_speed;
    }

    public double getM2_speed() {
        return m2_speed;
    }

    public void setM2_speed(double m2_speed) {
        this.m2_speed = m2_speed;
    }

    public double getM3_speed() {
        return m3_speed;
    }

    public void setM3_speed(double m3_speed) {
        this.m3_speed = m3_speed;
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

    public double getCurrent_pos_x() {
        return current_pos_x;
    }

    public void setCurrent_pos_x(double current_pos_x) {
        this.current_pos_x = current_pos_x;
    }

    public double getCurrent_pos_y() {
        return current_pos_y;
    }

    public void setCurrent_pos_y(double current_pos_y) {
        this.current_pos_y = current_pos_y;
    }

    public double getCurrent_pos_z() {
        return current_pos_z;
    }

    public void setCurrent_pos_z(double current_pos_z) {
        this.current_pos_z = current_pos_z;
    }

    public double getTarget_pos_x() {
        return target_pos_x;
    }

    public void setTarget_pos_x(double target_pos_x) {
        this.target_pos_x = target_pos_x;
    }

    public double getTarget_pos_y() {
        return target_pos_y;
    }

    public void setTarget_pos_y(double target_pos_y) {
        this.target_pos_y = target_pos_y;
    }

    public double getTarget_pos_z() {
        return target_pos_z;
    }

    public void setTarget_pos_z(double target_pos_z) {
        this.target_pos_z = target_pos_z;
    }
}
