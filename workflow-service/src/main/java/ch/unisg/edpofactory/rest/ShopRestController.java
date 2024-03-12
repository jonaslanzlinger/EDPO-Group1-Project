package ch.unisg.edpofactory.rest;

import ch.unisg.edpofactory.domain.Order;
import ch.unisg.edpofactory.dto.CamundaMessageDto;

import ch.unisg.edpofactory.messages.Message;
import ch.unisg.edpofactory.util.VariablesUtil;
import lombok.AllArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@AllArgsConstructor
public class ShopRestController {

    private final KafkaTemplate<String, CamundaMessageDto> kafkaTemplate;

    @RequestMapping(path = "/api/cart/order", method = PUT)
    public String placeOrder(@RequestParam(value = "customerId") String customerId) {

        Order order = new Order();
        order.setOrderId("firstOrder");

        Message<Order> message = new Message<>("OrderPlacedEvent", order);
        CamundaMessageDto dto = VariablesUtil.buildCamundaMessageDto(customerId, order);
        kafkaTemplate.send("EDPOFactory",dto);

        // note that we cannot easily return an order id here - as everything is asynchronous
        // and blocking the client is not what we want.
        // but we return an own correlationId which can be used in the UI to show status maybe later
        return "{\"traceId\": \"" + message.getTraceid() + "\"}";
    }

}