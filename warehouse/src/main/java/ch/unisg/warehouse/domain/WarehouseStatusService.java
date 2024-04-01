package ch.unisg.warehouse.domain;

import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This is a service class that manages the status of the warehouse.
 * It uses an AtomicReference to hold the latest status of the warehouse.
 */
@Service
public class WarehouseStatusService {

    // The latest status of the warehouse
    private final AtomicReference<HBW_1> latestStatus = new AtomicReference<>(new HBW_1());

    // if the warehouse is currently in use or not
    private final AtomicBoolean inUse = new AtomicBoolean(false);

    // que of waiting instances
    private final Queue<String> waitingProcesses = new ConcurrentLinkedQueue<>();

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


    public boolean isInUse() {
        return inUse.get();
    }

    public boolean tryReserveHBW() {
        synchronized (this) {
            if (!inUse.get()) {
                inUse.set(true);
                return true;
            }
            return false;
        }
    }

    public String releaseHBW() {
        inUse.set(false);
        return waitingProcesses.poll();
    }

    public synchronized void addToQueue(String processInstanceId) {
        waitingProcesses.add(processInstanceId);
    }

}