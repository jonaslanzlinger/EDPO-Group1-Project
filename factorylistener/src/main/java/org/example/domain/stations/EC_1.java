package org.example.domain.stations;

public class EC_1 extends Station {

    private String timestamp;
    private boolean i1_pos;
    private boolean i2_pos;
    private int i3_photoresistor;
    private boolean i5_joystick_x_f;
    private boolean i6_joystick_y_f;
    private boolean i7_joystick_x_b;
    private boolean i8_joystick_y_b;
    private String current_state;
    private String current_task;
    private double current_task_duration;
    private String current_sub_task;
    private String failure_label;

    public EC_1(String id, String station, String timestamp, boolean i1_pos, boolean i2_pos, int i3_photoresistor, boolean i5_joystick_x_f, boolean i6_joystick_y_f, boolean i7_joystick_x_b, boolean i8_joystick_y_b, String current_state, String current_task, double current_task_duration, String current_sub_task, String failure_label) {
        super(id, station);
        this.timestamp = timestamp;
        this.i1_pos = i1_pos;
        this.i2_pos = i2_pos;
        this.i3_photoresistor = i3_photoresistor;
        this.i5_joystick_x_f = i5_joystick_x_f;
        this.i6_joystick_y_f = i6_joystick_y_f;
        this.i7_joystick_x_b = i7_joystick_x_b;
        this.i8_joystick_y_b = i8_joystick_y_b;
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

    public boolean isI1_pos() {
        return i1_pos;
    }

    public void setI1_pos(boolean i1_pos) {
        this.i1_pos = i1_pos;
    }

    public boolean isI2_pos() {
        return i2_pos;
    }

    public void setI2_pos(boolean i2_pos) {
        this.i2_pos = i2_pos;
    }

    public int getI3_photoresistor() {
        return i3_photoresistor;
    }

    public void setI3_photoresistor(int i3_photoresistor) {
        this.i3_photoresistor = i3_photoresistor;
    }

    public boolean isI5_joystick_x_f() {
        return i5_joystick_x_f;
    }

    public void setI5_joystick_x_f(boolean i5_joystick_x_f) {
        this.i5_joystick_x_f = i5_joystick_x_f;
    }

    public boolean isI6_joystick_y_f() {
        return i6_joystick_y_f;
    }

    public void setI6_joystick_y_f(boolean i6_joystick_y_f) {
        this.i6_joystick_y_f = i6_joystick_y_f;
    }

    public boolean isI7_joystick_x_b() {
        return i7_joystick_x_b;
    }

    public void setI7_joystick_x_b(boolean i7_joystick_x_b) {
        this.i7_joystick_x_b = i7_joystick_x_b;
    }

    public boolean isI8_joystick_y_b() {
        return i8_joystick_y_b;
    }

    public void setI8_joystick_y_b(boolean i8_joystick_y_b) {
        this.i8_joystick_y_b = i8_joystick_y_b;
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