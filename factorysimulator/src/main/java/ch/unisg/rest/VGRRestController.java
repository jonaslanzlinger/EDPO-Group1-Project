package ch.unisg.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ch.unisg.domain.util.Utility.logInfo;

/**
 * This is the simulated REST controller for the VGR station.
 * Note: note all methods are included, since we don't need them all for our purposes.
 */
@RestController
@RequestMapping(path = "/vgr")
public class VGRRestController {

    @GetMapping(path = "/pick_up_and_transport")
    public ResponseEntity<Void> pickUpAndTransport() {
        logInfo("pickUpAndTransport", "Simulating the picking up and transporting of the order");
        return ResponseEntity.ok().build();
    }
}
