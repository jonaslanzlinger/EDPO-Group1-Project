package ch.unisg.monitoring.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the monitoring page.
 */
@Controller
@RequiredArgsConstructor
public class MonitoringController {

    @GetMapping("/")
    public String index() {
        return "index";
    }


    @GetMapping("/factory")
    public String updatesPage() {
        return "monitoring";
    }
}
