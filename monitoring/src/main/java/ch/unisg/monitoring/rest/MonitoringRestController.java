package ch.unisg.monitoring.rest;

import ch.unisg.monitoring.domain.MonitoringStore;
import ch.unisg.monitoring.kafka.dto.MonitorUpdateDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * This is a REST controller class for the Shop.
 * It uses Spring's @RestController annotation to indicate that it is a REST controller.
 * It uses Lombok's @AllArgsConstructor to generate a constructor with all properties.
 */
@RestController
@AllArgsConstructor
@Slf4j
public class MonitoringRestController {

    private final MonitoringStore monitoringStore;
    //TODO: Status codes
    @RequestMapping(path = "/api/monitoring/{orderId}", method = GET, produces = "application/json")
    public String getOrderId(@PathVariable String orderId) {

        // Check if the color is in stock
        if (!monitoringStore.containsOrder(orderId)) {
            return "{\"error\": \"Color not in stock\"}";
        }
        return monitoringStore.getMessages(orderId).stream().map(MonitorUpdateDto::toJson).collect(Collectors.joining(","));

    }

    @RequestMapping(path = "/api/monitoring/orders", method = GET, produces = "application/json")
    public String getOrders() {
        if(monitoringStore.isEmpty()) {
            return "{\"error\": \"No orders available\"}";
        }
        // Check if the color is in stock
        return monitoringStore.getAllMessages().values().stream().flatMap(l -> l.stream().map(MonitorUpdateDto::toJson)).collect(Collectors.joining(","));

    }
}