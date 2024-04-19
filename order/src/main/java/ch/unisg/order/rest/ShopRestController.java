package ch.unisg.order.rest;

import ch.unisg.order.domain.OrderRegistry;
import ch.unisg.order.services.ProcessStarterService;
import ch.unisg.order.domain.Order;

import ch.unisg.order.services.StockService;
import ch.unisg.order.utils.WorkflowLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

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
        OrderRegistry.addOrder(order);
        long messageKey = processStarterService.sendOrderReceivedMessage(order);
        stockService.removeColorFromStock(color);

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

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray = mapper.createArrayNode();

        OrderRegistry.getOrders().forEach(order -> {
            ObjectNode jsonObject = mapper.createObjectNode();
            jsonObject.put("orderId", order.getOrderId());
            jsonObject.put("progress", order.getProgress());
            jsonObject.put("color", order.getColor());
            jsonArray.add(jsonObject);
        });

        String jsonString = jsonArray.toString();

        try {
            emitter.send(SseEmitter.event().name("message").data(jsonString));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        emitter.complete();
        return emitter;
    }

    @ResponseBody
    @GetMapping("/api/currentStock")
    public SseEmitter sendCurrentStock() {
        SseEmitter emitter = new SseEmitter();
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode jsonObject1 = mapper.createObjectNode();
        jsonObject1.put("red", 0);
        jsonObject1.put("blue", 0);
        jsonObject1.put("white", 0);

        Map<String, String> stock = stockService.getStock();
        stock.forEach((key, value) -> {
            // check what color and then increase the count
            switch (value) {
                case "red" -> jsonObject1.put("red", jsonObject1.get("red").asInt() + 1);
                case "blue" -> jsonObject1.put("blue", jsonObject1.get("blue").asInt() + 1);
                case "white" -> jsonObject1.put("white", jsonObject1.get("white").asInt() + 1);
            }

        });

        String jsonString = jsonObject1.toString();

        try {
            emitter.send(SseEmitter.event().name("message").data(jsonString));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        emitter.complete();
        return emitter;
    }
}