package ch.unisg.warehouse.domain;

import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This is a service class that manages the status of the warehouse.
 * It uses an AtomicReference to hold the latest status of the warehouse.
 */
@Service
public class WareHouseStatusService {

    // The latest status of the warehouse
    private final AtomicReference<HBW_1> latestStatus = new AtomicReference<>(new HBW_1());

    /**
     * Updates the warehouse status.
     * @param hbw_1 The new status of the warehouse.
     */
    public void updateWarehouseStatus(HBW_1 hbw_1) {
        latestStatus.set(hbw_1);
    }

    /**
     * Retrieves the latest status of the warehouse.
     * @return The latest status of the warehouse.
     */
    public HBW_1 getLatestStatus() {
        return latestStatus.get();
    }

}