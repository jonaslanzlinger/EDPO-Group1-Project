package ch.unisg.domain.stations;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class EC_1 extends Station {

    private String timestamp;
    private double i1_pos;
    private double i2_pos;
    private int i3_photoresistor;
    private double i5_joystick_x_f;
    private double i6_joystick_y_f;
    private double i7_joystick_x_b;
    private double i8_joystick_y_b;
    private String current_state;
    private String current_task;
    private double current_task_duration;
    private String current_sub_task;
    private String failure_label;

}