package ch.unisg.domain.stations;

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isI3_pos_switch() {
        return i3_pos_switch;
    }

    public void setI3_pos_switch(boolean i3_pos_switch) {
        this.i3_pos_switch = i3_pos_switch;
    }

    public boolean isI4_pos_switch() {
        return i4_pos_switch;
    }

    public void setI4_pos_switch(boolean i4_pos_switch) {
        this.i4_pos_switch = i4_pos_switch;
    }

    public double getM2_speed() {
        return m2_speed;
    }

    public void setM2_speed(double m2_speed) {
        this.m2_speed = m2_speed;
    }

    public boolean isO5_valve() {
        return o5_valve;
    }

    public void setO5_valve(boolean o5_valve) {
        this.o5_valve = o5_valve;
    }

    public boolean isO6_valve() {
        return o6_valve;
    }

    public void setO6_valve(boolean o6_valve) {
        this.o6_valve = o6_valve;
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