package ch.unisg.domain.stations;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
@Setter
@Getter
@Data
@NoArgsConstructor
public class HBW_1 extends Station {

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

    public HBW_1(String id, String station, String timestamp, boolean i1_light_barrier, boolean i2_light_barrier, boolean i3_light_barrier, boolean i4_light_barrier, boolean i5_pos_switch, boolean i6_pos_switch, boolean i7_pos_switch, boolean i8_pos_switch, double m1_speed, double m2_speed, double m3_speed, double m4_speed, String current_state, String current_task, double current_task_duration, String current_sub_task, String failure_label, double current_pos_x, double current_pos_y, double target_pos_x, double target_pos_y, int amount_of_workpieces, Map<String, String> current_stock) {
        super(id, station);
        this.timestamp = timestamp;
        this.i1_light_barrier = i1_light_barrier;
        this.i2_light_barrier = i2_light_barrier;
        this.i3_light_barrier = i3_light_barrier;
        this.i4_light_barrier = i4_light_barrier;
        this.i5_pos_switch = i5_pos_switch;
        this.i6_pos_switch = i6_pos_switch;
        this.i7_pos_switch = i7_pos_switch;
        this.i8_pos_switch = i8_pos_switch;
        this.m1_speed = m1_speed;
        this.m2_speed = m2_speed;
        this.m3_speed = m3_speed;
        this.m4_speed = m4_speed;
        this.current_state = current_state;
        this.current_task = current_task;
        this.current_task_duration = current_task_duration;
        this.current_sub_task = current_sub_task;
        this.failure_label = failure_label;
        this.current_pos_x = current_pos_x;
        this.current_pos_y = current_pos_y;
        this.target_pos_x = target_pos_x;
        this.target_pos_y = target_pos_y;
        this.amount_of_workpieces = amount_of_workpieces;
        this.current_stock = current_stock;
    }

}