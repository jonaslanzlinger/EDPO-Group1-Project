package ch.unisg.delivery.domain;

import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This is a service class that manages the status of the delivery station.
 * It uses an AtomicReference to hold the latest status of the delivery station.
 */
@Service
public class DeliveryStatusService {

    // The latest status of the delivery station
    private final AtomicReference<VGR_1> latestStatus = new AtomicReference<>(new VGR_1());

    /**
     * Updates the delivery station status.
     * @param vgr_1 The new status of the delivery station.
     */
    public void updateDeliveryStatus(VGR_1 vgr_1) {
        latestStatus.set(vgr_1);
    }

    /**
     * Retrieves the latest status of the delivery station.
     * @return The latest status of the delivery station.
     */
    public VGR_1 getLatestStatus() {
        return latestStatus.get();
    }

}