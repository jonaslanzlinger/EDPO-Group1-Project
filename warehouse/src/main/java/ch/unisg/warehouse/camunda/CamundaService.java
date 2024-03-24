package ch.unisg.warehouse.camunda;

import io.camunda.zeebe.client.ZeebeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is a service class that provides utility methods for interacting with the Camunda Zeebe client.
 * It provides methods to send messages, throw errors, and complete jobs.
 */
@Service
public class CamundaService {

    // The Zeebe client instance used to interact with the Camunda engine
    private final ZeebeClient zeebeClient;

    /**
     * Constructor for the CamundaService class.
     * @param zeebeClient The Zeebe client instance to be used for interactions with the Camunda engine.
     */
    @Autowired
    public CamundaService(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    /**
     * Sends a message command to the Camunda engine.
     * @param messageName The name of the message to be sent.
     * @param orderId The correlation key for the message, usually something unique like an order ID.
     * @param orderDetailsJson The variables for the message, in JSON format.
     */
    public void sendMessageCommand(String messageName, String orderId, String orderDetailsJson) {
        zeebeClient.newPublishMessageCommand()
                .messageName(messageName)
                .correlationKey(orderId)
                .variables(orderDetailsJson)
                .send()
                .join(); // join() to synchronously wait for the result, remove for async
    }

    /**
     * Throws an error command to the Camunda engine.
     * @param errorCode The error code for the error to be thrown.
     * @param errorMessage The error message for the error to be thrown.
     * @param jobKey The key of the job where the error is to be thrown.
     */
    public void throwErrorCommand(String errorCode, String errorMessage, long jobKey) {
        zeebeClient.newThrowErrorCommand(jobKey)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .send()
                .join(); // join() to synchronously wait for the result, remove for async
    }

    /**
     * Sends a complete command to the Camunda engine.
     * @param jobKey The key of the job to be completed.
     * @param orderDetailsJson The variables for the job completion, in JSON format.
     */
    public void sendCompleteCommand(long jobKey, String orderDetailsJson) {
        zeebeClient.newCompleteCommand(jobKey)
                .variables(orderDetailsJson)
                .send()
                .join(); // join() to synchronously wait for the result, remove for async
    }
}