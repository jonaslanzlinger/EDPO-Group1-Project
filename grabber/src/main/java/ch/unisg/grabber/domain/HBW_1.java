package ch.unisg.grabber.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

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
public class HBW_1 {

    // The station of the HBW unit
    private String station;
    // The ID of the HBW unit
    private String id;
    // The timestamp of the HBW unit's status
    private String timestamp;
    // The status of the first light barrier
    private boolean i1_light_barrier;
    // The status of the second light barrier
    private boolean i2_light_barrier;
    // The status of the third light barrier
    private boolean i3_light_barrier;
    // The status of the fourth light barrier
    private boolean i4_light_barrier;
    // The status of the first position switch
    private boolean i5_pos_switch;
    // The status of the second position switch
    private boolean i6_pos_switch;
    // The status of the third position switch
    private boolean i7_pos_switch;
    // The status of the fourth position switch
    private boolean i8_pos_switch;
    // The speed of the first motor
    private double m1_speed;
    // The speed of the second motor
    private double m2_speed;
    // The speed of the third motor
    private double m3_speed;
    // The speed of the fourth motor
    private double m4_speed;
    // The current state of the HBW unit
    private String current_state;
    // The current task of the HBW unit
    private String current_task;
    // The duration of the current task
    private double current_task_duration;
    // The current sub-task of the HBW unit
    private String current_sub_task;
    // The failure label of the HBW unit
    private String failure_label;
    // The current X position of the HBW unit
    private double current_pos_x;
    // The current Y position of the HBW unit
    private double current_pos_y;
    // The target X position of the HBW unit
    private double target_pos_x;
    // The target Y position of the HBW unit
    private double target_pos_y;
    // The amount of workpieces in the HBW unit
    private int amount_of_workpieces;
    // The current stock of the HBW unit
    private Map<String, String> current_stock;
}