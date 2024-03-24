package ch.unisg.order.rest;

import ch.unisg.order.services.ProcessStarterService;
import ch.unisg.order.domain.Order;

import ch.unisg.order.util.WorkflowLogger;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * This is a REST controller class for the Shop.
 * It uses Spring's @RestController annotation to indicate that it is a REST controller.
 * It uses Lombok's @AllArgsConstructor to generate a constructor with all properties.
 */
@RestController
@AllArgsConstructor
@Slf4j
public class ShopRestController {

    // The service for starting processes
    private final ProcessStarterService processStarterService;

    /**
     * This method handles PUT requests to "/api/order/{color}".
     * It creates a new Order with the provided color, sends an order received message, and returns a JSON string with the order's traceId.
     * It uses Spring's @RequestMapping annotation to map the URL path and method to this method.
     * @param color The color of the order.
     * @return A JSON string with the order's traceId.
     */
    @RequestMapping(path = "/api/order/{color}", method = PUT)
    public String placeOrder(@PathVariable String color) {

        Order order = new Order(color);
        WorkflowLogger.info(log,"placeOrder","Order received: " + order.getOrderId() + " - " + order.getColor());
        processStarterService.sendOrderReceivedMessage(order.getOrderId(), order.getColor());
        return "{\"traceId\": \"" + order.getOrderId() + "\"}";
    }

}