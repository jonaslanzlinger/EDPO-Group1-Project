package ch.unisg.order.services;

import ch.unisg.order.domain.Order;
import ch.unisg.order.domain.OrderRegistry;
import ch.unisg.order.kafka.producer.MonitorDataProducer;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

import static ch.unisg.order.kafka.producer.MonitorDataProducer.MonitorStatus.*;
import static ch.unisg.order.utils.Utility.sleep;

/**
 * This is a service class for starting processes.
 * It uses Spring's @Service annotation to indicate that it is a service class.
 */
@Service
public class ProcessStarterService {

    // The Zeebe client for interacting with the Zeebe broker
    private final ZeebeClient zeebeClient;

    private final MonitorDataProducer monitorDataProducer;

    /**
     * Constructor with ZeebeClient parameter.
     * It initializes zeebeClient with the provided ZeebeClient.
     * It uses Spring's @Autowired annotation to automatically inject the ZeebeClient.
     *
     * @param zeebeClient         The ZeebeClient for interacting with the Zeebe broker.
     * @param monitorDataProducer
     */
    @Autowired
    public ProcessStarterService(ZeebeClient zeebeClient, MonitorDataProducer monitorDataProducer) {
        this.zeebeClient = zeebeClient;
        this.monitorDataProducer = monitorDataProducer;
    }

    /**
     * This method sends an order received message to the Zeebe broker.
     * It creates a new publish message command, sets the message name, correlation key and variables, and sends the command.
     *
     * @param order The order.
     * @return processInstanceKey
     */
    public long sendOrderReceivedMessage(Order order) {

        String orderVariables = "{\"order\": {\"orderColor\": \"" + order.getColor() + "\"," +
                "\"orderId\": \"" + order.getOrderId() + "\"," +
                "\"deliveryMethod\": \"" + order.getDeliveryMethod() + "\"}}";


        var returnvalue = zeebeClient.newPublishMessageCommand()
                .messageName("Msg_OrderReceived")
                .correlationKey(order.getOrderId()) // Usually, the correlationKey is something unique like orderId.
                .variables(orderVariables)
                .send()
                .join(); // join() to synchronously wait for the result, remove for async
        return returnvalue.getMessageKey();
    }

    /**
     * This method sends a message to the Zeebe broker to set the progress of an order to "warehouse".
     * It creates a new complete command, sets the variables, and sends the command.
     *
     * @param job The job that contains the details of the order.
     */
    @JobWorker(type = "setProgressWarehouse", name = "setProgressWarehouseProcessor", autoComplete = false)
    public void setProgressWarehouse(final ActivatedJob job) {
        Map<String, Object> orderFromJob = (Map<String, Object>) job.getVariablesAsMap().get("order");

        String orderIdJob = (String) orderFromJob.get("orderId");

        sleep(2000);
        Objects.requireNonNull(OrderRegistry.getOrderById(orderIdJob)).setProgress("warehouse");

        zeebeClient.newCompleteCommand(job.getKey())
                .variables(job.getVariables())
                .send()
                .join(); // join() to synchronously wait for the result, remove for async
        monitorDataProducer.sendMonitorUpdate(orderIdJob, "setProgressWarehouse", success.name());
    }

    /**
     * This method sends a message to the Zeebe broker to set the progress of an order to "grabber".
     * It creates a new complete command, sets the variables, and sends the command.
     *
     * @param job The job that contains the details of the order.
     */
    @JobWorker(type = "setProgressGrabber", name = "setProgressGrabberProcessor", autoComplete = false)
    public void setProgressGripper(final ActivatedJob job) {
        Map<String, Object> orderFromJob = (Map<String, Object>) job.getVariablesAsMap().get("order");

        String orderIdJob = (String) orderFromJob.get("orderId");

        sleep(2000);
        Objects.requireNonNull(OrderRegistry.getOrderById(orderIdJob)).setProgress("grabber");

        zeebeClient.newCompleteCommand(job.getKey())
                .variables(job.getVariables())
                .send()
                .join(); // join() to synchronously wait for the result, remove for async
        monitorDataProducer.sendMonitorUpdate(orderIdJob, "setProgressGrabber", success.name());
    }

    /**
     * This method sends a message to the Zeebe broker to set the progress of an order to "delivery".
     * It creates a new complete command, sets the variables, and sends the command.
     *
     * @param job The job that contains the details of the order.
     */
    @JobWorker(type = "setProgressDelivery", name = "setProgressDeliveryProcessor", autoComplete = false)
    public void setProgressDelivery(final ActivatedJob job) {
        Map<String, Object> orderFromJob = (Map<String, Object>) job.getVariablesAsMap().get("order");

        String orderIdJob = (String) orderFromJob.get("orderId");

        sleep(2000);
        Objects.requireNonNull(OrderRegistry.getOrderById(orderIdJob)).setProgress("delivery");

        zeebeClient.newCompleteCommand(job.getKey())
                .variables(job.getVariables())
                .send()
                .join(); // join() to synchronously wait for the result, remove for async
        monitorDataProducer.sendMonitorUpdate(orderIdJob, "setProgressDelivery", success.name());
    }

    /**
     * This method sends a message to the Zeebe broker to set the progress of an order to "delivered".
     * It creates a new complete command, sets the variables, and sends the command.
     *
     * @param job The job that contains the details of the order.
     */
    @JobWorker(type = "setProgressDelivered", name = "setProgressDeliveredProcessor", autoComplete = false)
    public void setProgressDelivered(final ActivatedJob job) {
        Map<String, Object> orderFromJob = (Map<String, Object>) job.getVariablesAsMap().get("order");

        String orderIdJob = (String) orderFromJob.get("orderId");

        sleep(2000);
        Objects.requireNonNull(OrderRegistry.getOrderById(orderIdJob)).setProgress("delivered");

        zeebeClient.newCompleteCommand(job.getKey())
                .variables(job.getVariables())
                .send()
                .join(); // join() to synchronously wait for the result, remove for async

        monitorDataProducer.sendMonitorUpdate(orderIdJob, "setProgressDelivered", success.name());
    }
}