package ch.unisg.delivery.domain;

import ch.unisg.delivery.camunda.CamundaService;
import ch.unisg.delivery.utils.WorkflowLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if (vgr_1.getI8_color_sensor() <= 1550) {
            try {
                String orderId = OrderRegistry.pop().getOrderId();
                camundaMessageSenderService.sendMessageCommand(
                        "ProductAtLightSensor",
                        orderId);
            } catch (NullPointerException ignore) {} finally {
                WorkflowLogger.info(log, "updateDeliveryStatus", "Light sensor triggered");
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