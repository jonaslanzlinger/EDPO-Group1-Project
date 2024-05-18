package ch.unisg.warehouse.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * This is a data class that represents a High Bay Warehouse (HBW) unit.
 * It uses Lombok annotations for automatic generation of getters, setters, constructors, and builder.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HBW_1 {

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
}