package ch.unisg.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ch.unisg.domain.util.Utility.logInfo;

/**
 * This is the simulated REST controller for the HBW station.
 */
@RestController
@RequestMapping(path = "/hbw")
public class HBWRestController {

    @GetMapping(path = "/calibrate")
    public ResponseEntity<Void> hbwCalibrate() {
        logInfo("hbwCalibrate", "Simulating HBW calibrate");
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/change_buckets")
    public ResponseEntity<Void> hbwChangeBuckets() {
        logInfo("hbwChangeBuckets", "Simulating HBW change buckets");
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/get_amount_of_stored_workpieces")
    public ResponseEntity<Integer> hbwGetAmountOfStoredWorkpieces() {
        logInfo("hbwGetAmountOfStoredWorkpieces", "Simulating HBW get amount of stored workpieces");
        return ResponseEntity.ok(0);
    }

    @GetMapping(path = "/get_motor_speed")
    public ResponseEntity<Integer> hbwGetMotorSpeed() {
        logInfo("hbwGetMotorSpeed", "Simulating HBW get motor speed");
        return ResponseEntity.ok(0);
    }

    @GetMapping(path = "/get_slot_number_of_workpiece_by_color")
    public ResponseEntity<Integer> hbwGetSlotNumberOfWorkpieceByColor() {
        logInfo("hbwGetSlotNumberOfWorkpieceByColor", "Simulating HBW get slot number of workpiece by color");
        return ResponseEntity.ok(0);
    }

    @GetMapping(path = "/get_workpiece_by_color")
    public ResponseEntity<Integer> hbwGetWorkpieceByColor() {
        logInfo("hbwGetWorkpieceByColor", "Simulating HBW get workpiece by color");
        return ResponseEntity.ok(0);
    }

    @GetMapping(path = "/reset_all_motor_speeds")
    public ResponseEntity<Void> hbwResetAllMotorSpeeds() {
        logInfo("hbwResetAllMotorSpeeds", "Simulating HBW reset all motor speeds");
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/set_motor_speed")
    public ResponseEntity<Void> hbwSetMotorSpeed() {
        logInfo("hbwSetMotorSpeed", "Simulating HBW set motor speed");
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/state_of_machine")
    public ResponseEntity<Void> hbwGetStateOfMachine() {
        logInfo("hbwGetStateOfMachine", "Simulating HBW get state of machine");
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/status_of_light_barrier")
    public ResponseEntity<Void> hbwGetStatusOfLightBarrier() {
        logInfo("hbwGetStatusOfLightBarrier", "Simulating HBW get status of light barrier");
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/store")
    public ResponseEntity<Void> hbwStore() {
        logInfo("hbwStore", "Simulating HBW store");
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/unload")
    public ResponseEntity<Void> hbwUnload() {
        logInfo("hbwUnload", "Simulating HBW unload");
        return ResponseEntity.ok().build();
    }
}
