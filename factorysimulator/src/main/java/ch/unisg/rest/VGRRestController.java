package ch.unisg.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ch.unisg.domain.util.Utility.logInfo;


@RestController
@RequestMapping(path = "/vgr")
public class VGRRestController {



    @GetMapping(path = "/pick_up_and_transport")
    public ResponseEntity<Void> pickUpAndTransport() {
        logInfo("pickUpAndTransport", "Picking up and transporting the order");
        return ResponseEntity.ok().build();
    }
}
