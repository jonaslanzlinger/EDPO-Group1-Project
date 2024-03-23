package ch.unisg.order.services;

import io.camunda.zeebe.client.ZeebeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessStarterService {

    private final ZeebeClient zeebeClient;

    @Autowired
    public ProcessStarterService(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    public void sendOrderReceivedMessage(String orderId, String orderDetails) {
        zeebeClient.newPublishMessageCommand()
                .messageName("Msg_OrderReceived")
                .correlationKey(orderId) // Usually, the correlationKey is something unique like orderId.
                .variables("{\"orderDetails\":\"" + orderDetails + "\"}")
                .send()
                .join(); // join() to synchronously wait for the result, remove for async
    }
}
