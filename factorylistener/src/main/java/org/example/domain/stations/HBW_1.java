package org.example.domain.stations;

import java.util.Map;

public class HBW_1 extends Station {

    private String timestamp;
    private boolean i1_light_barrier;
    private boolean i2_light_barrier;
    private boolean i3_light_barrier;
    private boolean i4_light_barrier;
    private boolean i5_pos_switch;
    private boolean i6_pos_switch;
    private boolean i7_pos_switch;
    private boolean i8_pos_switch;
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

    public HBW_1(String id, String station, String timestamp, boolean i1_light_barrier, boolean i2_light_barrier, boolean i3_light_barrier, boolean i4_light_barrier, boolean i5_pos_switch, boolean i6_pos_switch, boolean i7_pos_switch, boolean i8_pos_switch, double m1_speed, double m2_speed, double m3_speed, double m4_speed, String current_state, String current_task, double current_task_duration, String current_sub_task, String failure_label, double current_pos_x, double current_pos_y, double target_pos_x, double target_pos_y, int amount_of_workpieces, Map<String, String> current_stock) {
        super(id, station);
        this.timestamp = timestamp;
        this.i1_light_barrier = i1_light_barrier;
        this.i2_light_barrier = i2_light_barrier;
        this.i3_light_barrier = i3_light_barrier;
        this.i4_light_barrier = i4_light_barrier;
        this.i5_pos_switch = i5_pos_switch;
        this.i6_pos_switch = i6_pos_switch;
        this.i7_pos_switch = i7_pos_switch;
        this.i8_pos_switch = i8_pos_switch;
        this.m1_speed = m1_speed;
        this.m2_speed = m2_speed;
        this.m3_speed = m3_speed;
        this.m4_speed = m4_speed;
        this.current_state = current_state;
        this.current_task = current_task;
        this.current_task_duration = current_task_duration;
        this.current_sub_task = current_sub_task;
        this.failure_label = failure_label;
        this.current_pos_x = current_pos_x;
        this.current_pos_y = current_pos_y;
        this.target_pos_x = target_pos_x;
        this.target_pos_y = target_pos_y;
        this.amount_of_workpieces = amount_of_workpieces;
        this.current_stock = current_stock;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isI1_light_barrier() {
        return i1_light_barrier;
    }

    public void setI1_light_barrier(boolean i1_light_barrier) {
        this.i1_light_barrier = i1_light_barrier;
    }

    public boolean isI2_light_barrier() {
        return i2_light_barrier;
    }

    public void setI2_light_barrier(boolean i2_light_barrier) {
        this.i2_light_barrier = i2_light_barrier;
    }

    public boolean isI3_light_barrier() {
        return i3_light_barrier;
    }

    public void setI3_light_barrier(boolean i3_light_barrier) {
        this.i3_light_barrier = i3_light_barrier;
    }

    public boolean isI4_light_barrier() {
        return i4_light_barrier;
    }

    public void setI4_light_barrier(boolean i4_light_barrier) {
        this.i4_light_barrier = i4_light_barrier;
    }

    public boolean isI5_pos_switch() {
        return i5_pos_switch;
    }

    public void setI5_pos_switch(boolean i5_pos_switch) {
        this.i5_pos_switch = i5_pos_switch;
    }

    public boolean isI6_pos_switch() {
        return i6_pos_switch;
    }

    public void setI6_pos_switch(boolean i6_pos_switch) {
        this.i6_pos_switch = i6_pos_switch;
    }

    public boolean isI7_pos_switch() {
        return i7_pos_switch;
    }

    public void setI7_pos_switch(boolean i7_pos_switch) {
        this.i7_pos_switch = i7_pos_switch;
    }

    public boolean isI8_pos_switch() {
        return i8_pos_switch;
    }

    public void setI8_pos_switch(boolean i8_pos_switch) {
        this.i8_pos_switch = i8_pos_switch;
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

    public double getM4_speed() {
        return m4_speed;
    }

    public void setM4_speed(double m4_speed) {
        this.m4_speed = m4_speed;
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

    public int getAmount_of_workpieces() {
        return amount_of_workpieces;
    }

    public void setAmount_of_workpieces(int amount_of_workpieces) {
        this.amount_of_workpieces = amount_of_workpieces;
    }

    public Map<String, String> getCurrent_stock() {
        return current_stock;
    }

    public void setCurrent_stock(Map<String, String> current_stock) {
        this.current_stock = current_stock;
    }
}