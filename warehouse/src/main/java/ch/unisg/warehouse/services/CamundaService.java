package ch.unisg.warehouse.services;

import io.camunda.zeebe.client.ZeebeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CamundaService {


    private final ZeebeClient zeebeClient;

    @Autowired
    public CamundaService(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    public void sendMessageCommand(String messageName, String orderId, String orderDetailsJson) {
        zeebeClient.newPublishMessageCommand()
                .messageName(messageName)
                .correlationKey(orderId) // Usually, the correlationKey is something unique like orderId.
                .variables(orderDetailsJson)
                .send()
                .join(); // join() to synchronously wait for the result, remove for async
    }

    public void throwErrorCommand(String errorCode, String errorMessage, long jobKey) {
        zeebeClient.newThrowErrorCommand(jobKey)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .send()
                .join(); // join() to synchronously wait for the result, remove for async
    }

    public void sendCompleteCommand(long jobKey, String orderDetailsJson) {
        zeebeClient.newCompleteCommand(jobKey)
                .variables(orderDetailsJson)
                .send()
                .join(); // join() to synchronously wait for the result, remove for async
    }

    public void startSubProcess(String processId, String orderDetailsJson) {
        zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId(processId)
                .latestVersion()
                .variables(orderDetailsJson)
                .send()
                .join(); // join() to synchronously wait for the result, remove for async
    }
}
