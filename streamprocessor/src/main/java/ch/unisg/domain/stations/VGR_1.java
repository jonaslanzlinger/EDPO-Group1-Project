package ch.unisg.domain.stations;


import lombok.*;

import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VGR_1  {

    String id;
    String station;
    private Instant timestamp;

    private boolean i1_pos_switch;

    private boolean i2_pos_switch;

    private boolean i3_pos_switch;

    private boolean i7_light_barrier;

    private boolean i4_light_barrier;

    private double i8_color_sensor;

    private String color;

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


}
