package ch.unisg.order.rest;

import ch.unisg.order.services.ProcessStarterService;
import ch.unisg.order.domain.Order;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@AllArgsConstructor
public class ShopRestController {

    private final ProcessStarterService processStarterService;

    @RequestMapping(path = "/api/order/{color}", method = PUT)
    public String placeOrder(@PathVariable String color) {

        Order order = new Order(color);
        processStarterService.sendOrderReceivedMessage(order.getOrderId(), order.getColor());
        return "{\"traceId\": \"" + order.getOrderId() + "\"}";
    }

}