package ch.unisg.domain.stations;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
