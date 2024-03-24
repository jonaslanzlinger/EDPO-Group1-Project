package ch.unisg.grabber.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is a domain class for VGR_1.
 * It uses Lombok's @Data annotation to generate getters, setters, equals, hashCode and toString methods.
 * It uses Lombok's @Builder annotation to provide a builder pattern for object creation.
 * It uses Lombok's @AllArgsConstructor to generate a constructor with all properties.
 * It uses Lombok's @NoArgsConstructor to generate a no-args constructor.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VGR_1 {

    // The station of the VGR_1
    private String station;

    // The id of the VGR_1
    private String id;

    // The timestamp of the VGR_1
    private String timestamp;

    // Position switch 1 of the VGR_1
    private boolean i1_pos_switch;

    // Position switch 2 of the VGR_1
    private boolean i2_pos_switch;

    // Position switch 3 of the VGR_1
    private boolean i3_pos_switch;

    // Light barrier 1 of the VGR_1
    private boolean i7_light_barrier;

    // Light barrier 2 of the VGR_1
    private boolean i4_light_barrier;

    // The color sensor of the VGR_1
    private double i8_color_sensor;

    // The compressor level of the VGR_1
    private double o7_compressor_level;

    // The valve open status of the VGR_1
    private boolean o8_valve_open;

    // The speed of motor 1 of the VGR_1
    private double m1_speed;

    // The speed of motor 2 of the VGR_1
    private double m2_speed;

    // The speed of motor 3 of the VGR_1
    private double m3_speed;

    // The current state of the VGR_1
    private String current_state;

    // The current task of the VGR_1
    private String current_task;

    // The duration of the current task of the VGR_1
    private double current_task_duration;

    // The current subtask of the VGR_1
    private String current_sub_task;

    // The failure label of the VGR_1
    private String failure_label;

    // The current x position of the VGR_1
    private double current_pos_x;

    // The current y position of the VGR_1
    private double current_pos_y;

    // The current z position of the VGR_1
    private double current_pos_z;

    // The target x position of the VGR_1
    private double target_pos_x;

    // The target y position of the VGR_1
    private double target_pos_y;

    // The target z position of the VGR_1
    private double target_pos_z;
}