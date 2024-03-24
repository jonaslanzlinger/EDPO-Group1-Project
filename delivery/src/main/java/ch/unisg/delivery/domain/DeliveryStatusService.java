package ch.unisg.delivery.domain;

import ch.unisg.delivery.camunda.CamundaService;
import ch.unisg.delivery.utils.WorkflowLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This is a service class that manages the status of the delivery station.
 * It uses an AtomicReference to hold the latest status of the delivery station.
 */
@Service
@Slf4j
public class DeliveryStatusService {

    // The latest status of the delivery station
    private final AtomicReference<VGR_1> latestStatus = new AtomicReference<>(new VGR_1());

    // The service for sending messages to Camunda
    @Autowired
    private CamundaService camundaMessageSenderService;

    /**
     * Updates the delivery station status.
     * This method is called when a message is received from the Kafka topic "VGR_1".
     * It updates the delivery station status with the data from the message.
     * if the light sensor has been triggered, it pops an order from the order registry and sends a message to the Camunda engine.
     * @param vgr_1 The new status of the delivery station.
     */
    public void updateDeliveryStatus(VGR_1 vgr_1) {
        latestStatus.set(vgr_1);

        // TODO don't know which sensor is which. Therefore, listen on both...
        if (vgr_1.isI4_light_barrier() || vgr_1.isI7_light_barrier()) {
            try {
                Order order = OrderRegistry.pop();

                WorkflowLogger.info(log, "updateDeliveryStatus","Light sensor triggered. Order popped from registry: " + order.getOrderId());

                camundaMessageSenderService.sendMessageCommand(
                        "Msg_ProductAtLightSensor",
                        order.getOrderId(),
                        String.format("{\"orderDetails\": \"%s\"}", order.getColor()));
            } catch (IndexOutOfBoundsException e) {
                // no orders in the registry
            }
        }
    }

    /**
     * Retrieves the latest status of the delivery station.
     * @return The latest status of the delivery station.
     */
    public VGR_1 getLatestStatus() {
        return latestStatus.get();
    }

}