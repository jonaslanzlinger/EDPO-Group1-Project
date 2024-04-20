package ch.unisg.order.services;

import ch.unisg.order.domain.Order;
import ch.unisg.order.domain.OrderRegistry;
import ch.unisg.order.kafka.producer.MonitorDataProducer;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     *
     * @param order The order.
     */
    @JobWorker(type = "setProgressWarehouse", name = "setProgressWarehouseProcessor")
    public void setProgressWarehouse(@Variable Order order) {
        sleep(2000);
        Objects.requireNonNull(OrderRegistry.getOrderById(order.getOrderId())).setProgress("warehouse");

        monitorDataProducer.sendMonitorUpdate(order.getOrderId(), "setProgressWarehouse", success.name());
    }

    /**
     * This method sends a message to the Zeebe broker to set the progress of an order to "grabber".
     *
     * @param order The order.
     */
    @JobWorker(type = "setProgressGrabber", name = "setProgressGrabberProcessor")
    public void setProgressGrabber(@Variable Order order) {
        sleep(2000);
        Objects.requireNonNull(OrderRegistry.getOrderById(order.getOrderId())).setProgress("grabber");

        monitorDataProducer.sendMonitorUpdate(order.getOrderId(), "setProgressGrabber", success.name());
    }

    /**
     * This method sends a message to the Zeebe broker to set the progress of an order to "delivery".
     *
     * @param order The order.
     */
    @JobWorker(type = "setProgressDelivery", name = "setProgressDeliveryProcessor")
    public void setProgressDelivery(@Variable Order order) {
        sleep(2000);
        Objects.requireNonNull(OrderRegistry.getOrderById(order.getOrderId())).setProgress("delivery");

        monitorDataProducer.sendMonitorUpdate(order.getOrderId(), "setProgressDelivery", success.name());
    }

    /**
     * This method sends a message to the Zeebe broker to set the progress of an order to "delivered".
     *
     * @param order The order.
     */
    @JobWorker(type = "setProgressDelivered", name = "setProgressDeliveredProcessor")
    public void setProgressDelivered(@Variable Order order) {
        sleep(2000);
        Objects.requireNonNull(OrderRegistry.getOrderById(order.getOrderId())).setProgress("delivered");

        monitorDataProducer.sendMonitorUpdate(order.getOrderId(), "setProgressDelivered", success.name());
    }

    /**
     * This method sends a message to the Zeebe broker to set the progress of an order to "failure".
     *
     * @param order The order.
     *
     */
    @JobWorker(type = "setProgressFailure", name = "setProgressFailureProcessor")
    public void setProgressFailure(@Variable Order order) {
        Objects.requireNonNull(OrderRegistry.getOrderById(order.getOrderId())).setProgress("failure");

        monitorDataProducer.sendMonitorUpdate(order.getOrderId(), "setProgressFailure", success.name());
    }


}