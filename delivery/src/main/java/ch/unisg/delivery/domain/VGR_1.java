package ch.unisg.delivery.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is a data class that represents a VGR_1 unit.
 * It uses Lombok annotations for automatic generation of getters, setters, constructors, and builder.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VGR_1 {

    // The station of the VGR unit
    private String station;
    // The ID of the VGR unit
    private String id;
    // The timestamp of the VGR unit's status
    private String timestamp;
    // The status of the first position switch
    private boolean i1_pos_switch;
    // The status of the second position switch
    private boolean i2_pos_switch;
    // The status of the third position switch
    private boolean i3_pos_switch;
    // The status of the first light barrier
    private boolean i7_light_barrier;
    // The status of the second light barrier
    private boolean i4_light_barrier;
    // The status of the color sensor
    private double i8_color_sensor;
    // The compressor level
    private double o7_compressor_level;
    // The status of the valve
    private boolean o8_valve_open;
    // The speed of the first motor
    private double m1_speed;
    // The speed of the second motor
    private double m2_speed;
    // The speed of the third motor
    private double m3_speed;
    // The current state of the station
    private String current_state;
    // The current task of the station
    private String current_task;
    // The duration of the current task
    private double current_task_duration;
    // The current sub-task of the station
    private String current_sub_task;
    // The failure label of the station
    private String failure_label;
    // The current X position of the station
    private double current_pos_x;
    // The current Y position of the station
    private double current_pos_y;
    // The current Z position of the station
    private double current_pos_z;
    // The target X position of the station
    private double target_pos_x;
    // The target Y position of the station
    private double target_pos_y;
    // The target Z position of the station
    private double target_pos_z;
}
