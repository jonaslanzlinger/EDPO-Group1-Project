package ch.unisg.domain.stations;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}