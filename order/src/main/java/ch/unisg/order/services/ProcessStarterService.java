package ch.unisg.order.services;

import ch.unisg.order.domain.Order;
import io.camunda.zeebe.client.ZeebeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is a service class for starting processes.
 * It uses Spring's @Service annotation to indicate that it is a service class.
 */
@Service
public class ProcessStarterService {

    // The Zeebe client for interacting with the Zeebe broker
    private final ZeebeClient zeebeClient;

    /**
     * Constructor with ZeebeClient parameter.
     * It initializes zeebeClient with the provided ZeebeClient.
     * It uses Spring's @Autowired annotation to automatically inject the ZeebeClient.
     * @param zeebeClient The ZeebeClient for interacting with the Zeebe broker.
     */
    @Autowired
    public ProcessStarterService(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    /**
     * This method sends an order received message to the Zeebe broker.
     * It creates a new publish message command, sets the message name, correlation key and variables, and sends the command.
     * @param order      The order.
     * @return processInstanceKey
     */
    public long sendOrderReceivedMessage(Order order) {

        String orderVariables = "{\"orderColor\": \"" + order.getColor() + "\"," +
                "\"orderId\": \"" + order.getOrderId() + "\"," +
                "\"deliveryMethod\": \"" + order.getDeliveryMethod() + "\"}";


        var returnvalue = zeebeClient.newPublishMessageCommand()
                .messageName("Msg_OrderReceived")
                .correlationKey(order.getOrderId()) // Usually, the correlationKey is something unique like orderId.
                .variables(orderVariables)
                .send()
                .join(); // join() to synchronously wait for the result, remove for async
        return returnvalue.getMessageKey();
    }
}