package org.example.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping(path = "/hbw")
public class HBWRestController {



    @GetMapping(path = "/calibrate")
    public ResponseEntity<Void> hbwCalibrate() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/change_buckets")
    public ResponseEntity<Void> hbwChangeBuckets() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/get_amount_of_stored_workpieces")
    public ResponseEntity<Integer> hbwGetAmountOfStoredWorkpieces() {
        return ResponseEntity.ok(0);
    }

    @GetMapping(path = "/get_motor_speed")
    public ResponseEntity<Integer> hbwGetMotorSpeed() {
        return ResponseEntity.ok(0);
    }

    @GetMapping(path = "/get_slot_number_of_workpiece_by_color")
    public ResponseEntity<Integer> hbwGetSlotNumberOfWorkpieceByColor() {
        return ResponseEntity.ok(0);
    }

    @GetMapping(path = "/get_workpiece_by_color")
    public ResponseEntity<Integer> hbwGetWorkpieceByColor() {
        return ResponseEntity.ok(0);
    }

    @GetMapping(path = "/reset_all_motor_speeds")
    public ResponseEntity<Void> hbwResetAllMotorSpeeds() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/set_motor_speed")
    public ResponseEntity<Void> hbwSetMotorSpeed() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/state_of_machine")
    public ResponseEntity<Void> hbwGetStateOfMachine() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/status_of_light_barrier")
    public ResponseEntity<Void> hbwGetStatusOfLightBarrier() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/store")
    public ResponseEntity<Void> hbwStore() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/unload")
    public ResponseEntity<Void> hbwUnload() {
        return ResponseEntity.ok().build();
    }
}
