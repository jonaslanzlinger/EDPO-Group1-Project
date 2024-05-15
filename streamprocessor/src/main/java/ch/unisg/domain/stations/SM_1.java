package ch.unisg.domain.stations;

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

    public int getI2_color_sensor() {
        return i2_color_sensor;
    }

    public void setI2_color_sensor(int i2_color_sensor) {
        this.i2_color_sensor = i2_color_sensor;
    }

    public boolean isI3_light_barrier() {
        return i3_light_barrier;
    }

    public void setI3_light_barrier(boolean i3_light_barrier) {
        this.i3_light_barrier = i3_light_barrier;
    }

    public boolean isI6_light_barrier() {
        return i6_light_barrier;
    }

    public void setI6_light_barrier(boolean i6_light_barrier) {
        this.i6_light_barrier = i6_light_barrier;
    }

    public boolean isI7_light_barrier() {
        return i7_light_barrier;
    }

    public void setI7_light_barrier(boolean i7_light_barrier) {
        this.i7_light_barrier = i7_light_barrier;
    }

    public boolean isI8_light_barrier() {
        return i8_light_barrier;
    }

    public void setI8_light_barrier(boolean i8_light_barrier) {
        this.i8_light_barrier = i8_light_barrier;
    }

    public double getM1_speed() {
        return m1_speed;
    }

    public void setM1_speed(double m1_speed) {
        this.m1_speed = m1_speed;
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