package ch.unisg.warehouse.services;

import io.camunda.zeebe.client.ZeebeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CamundaMessageSenderService {


    private final ZeebeClient zeebeClient;

    @Autowired
    public CamundaMessageSenderService(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    public void sendMessage(String messageName, String orderId, String orderDetailsJson) {
        zeebeClient.newPublishMessageCommand()
                .messageName(messageName)
                .correlationKey(orderId) // Usually, the correlationKey is something unique like orderId.
                .variables(orderDetailsJson)
                .send()
                .join(); // join() to synchronously wait for the result, remove for async
    }
}
