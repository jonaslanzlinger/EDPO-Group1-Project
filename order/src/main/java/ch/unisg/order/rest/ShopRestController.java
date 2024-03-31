package ch.unisg.order.rest;

import ch.unisg.order.services.ProcessStarterService;
import ch.unisg.order.domain.Order;

import ch.unisg.order.services.StockService;
import ch.unisg.order.util.WorkflowLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Random;

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

    private final StockService stockService;

    /**
     * This method handles PUT requests to "/api/order/{color}/{deliveryMethod}".
     * It creates a new Order with the provided color and the deliveryMethod, sends an order received message, and returns a JSON string with the order's traceId.
     * It uses Spring's @RequestMapping annotation to map the URL path and method to this method.
     * @param color The color of the order.
     * @param deliveryMethod The delivery method of the order.
     * @return A JSON string with the order's traceId.
     */
    @RequestMapping(path = "/api/order/{color}/{deliveryMethod}", method = PUT)
    public String placeOrder(@PathVariable String color, @PathVariable String deliveryMethod) {


        // Check if the color is in stock
        if (!stockService.checkStock(color)) {
            return "{\"error\": \"Color not in stock\"}";
        }


        Order order = new Order(color, deliveryMethod);
        long messageKey = processStarterService.sendOrderReceivedMessage(order);

        // TODO: CHECK WHY THIS WORKS?
        long processInstanceKey = messageKey + 1;

        WorkflowLogger.info(log,"placeOrder","Order received: " + processInstanceKey + " - " + order.getColor());
        return "{\"traceId\": \"" + order.getOrderId() + "\"}";
    }

    /**
     * This method handles GET requests to "/updates".
     * It sends an update message and returns an SseEmitter.
     * It uses Spring's @GetMapping annotation to map the URL path and method to this method.
     * @return An SseEmitter.
     */
    @GetMapping("/api/updates")
    public SseEmitter sendUpdates() {
        SseEmitter emitter = new SseEmitter();

        // TODO insert some real data objects here. just mock data for now
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray = mapper.createArrayNode();

        ObjectNode jsonObject1 = mapper.createObjectNode();
        jsonObject1.put("orderId", new Random().nextInt(1000000));
        jsonObject1.put("station", "warehouse");
        jsonObject1.put("color", "blue");
        jsonArray.add(jsonObject1);

        ObjectNode jsonObject2 = mapper.createObjectNode();
        jsonObject2.put("orderId", new Random().nextInt(1000000));
        jsonObject2.put("station", "gripper");
        jsonObject2.put("color", "red");
        jsonArray.add(jsonObject2);

        ObjectNode jsonObject3 = mapper.createObjectNode();
        jsonObject3.put("orderId", new Random().nextInt(1000000));
        jsonObject3.put("station", "delivery");
        jsonObject3.put("color", "white");
        jsonArray.add(jsonObject3);

        String jsonString = jsonArray.toString();

        try {
            emitter.send(SseEmitter.event().name("message").data(jsonString));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        emitter.complete();
        return emitter;
    }

}