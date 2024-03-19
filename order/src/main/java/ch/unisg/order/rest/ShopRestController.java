package ch.unisg.order.rest;

import ch.unisg.order.services.MessageService;
import ch.unisg.order.domain.Order;
import ch.unisg.order.dto.CamundaMessageDto;

import ch.unisg.order.util.VariablesUtil;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@AllArgsConstructor
public class ShopRestController {

    private final MessageService messageService;
    private final static String MESSAGE_START = "Order";

    @RequestMapping(path = "/api/order", method = PUT)
    public String placeOrder() {

        Order order = new Order();

        CamundaMessageDto message = VariablesUtil.buildCamundaMessageDto(order.getOrderId(), order);
        messageService.correlateMessage(message, MESSAGE_START);

        System.out.println();

        return "{\"traceId\": \"" + message.getDto().getOrderId() + "\"}";
    }

}