package ch.unisg.delivery.rest;

import ch.unisg.delivery.camunda.CamundaService;
import ch.unisg.delivery.domain.DeliveryStatusService;
import ch.unisg.delivery.domain.Order;
import ch.unisg.delivery.domain.OrderRegistry;
import ch.unisg.delivery.domain.VGR_1;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This is a REST controller class for the Delivery Service.
 * It uses Spring's @RestController annotation to indicate that it is a REST controller.
 * It uses Lombok's @AllArgsConstructor to generate a constructor with all properties.
 */
@RestController
@AllArgsConstructor
@Slf4j
public class DeliveryServiceRestController {

    // The service for sending messages to Camunda
    private CamundaService camundaMessageSenderService;

    // The service for updating the delivery status
    private DeliveryStatusService deliveryStatusService;

    /**
     * This method returns the latest status of the delivery station.
     * @return The latest status of the delivery station.
     */
    @GetMapping(path = "/status", produces = "application/json")
    public VGR_1 getStatus() {
        return deliveryStatusService.getLatestStatus();
    }

    /**
     * This method returns the list of orders that have been placed.
     * @return The list of orders that have been placed.
     */
    @GetMapping(path = "/orders", produces = "application/json")
    public List<Order> getOrders() {
        return OrderRegistry.getOrders();
    }

    // TODO: include orderId in the variables
    /**
     * This method triggers the light sensor in the delivery station.
     * It pops an order from the order registry and sends a message to the Camunda engine.
     * @return A message indicating that the light sensor has been triggered.
     */
    @GetMapping("/triggerLightSensor")
    public String triggerLightSensor(){
        Order order = OrderRegistry.pop();
        camundaMessageSenderService.sendMessageCommand(
                "Msg_ProductAtLightSensor",
                order.getOrderColor(),
                String.format("{ \"orderId\": \"%s\", " +
                              "\"orderColor\": \"%s\", " +
                              "\"deliveryMethod\": \"%s\" }", order.getOrderId(), order.getOrderColor(), order.getDeliveryMethod()));
        return String.format("Light sensor triggered for order: %s", order);
    }
}
