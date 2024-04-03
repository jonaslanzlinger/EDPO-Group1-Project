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

    // Queue of waiting instances
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

    /**
     * Checks if the warehouse is currently in use.
     * @return true if the warehouse is in use, false otherwise.
     */
    public boolean isInUse() {
        return inUse.get();
    }

    /**
     * Tries to reserve the warehouse. If the warehouse is not in use, it sets it to in use and returns true.
     * If the warehouse is in use, it returns false.
     * @return true if the warehouse was successfully reserved, false otherwise.
     */
    public boolean tryReserveHBW() {
        synchronized (this) {
            if (!inUse.get()) {
                inUse.set(true);
                return true;
            }
            return false;
        }
    }

    /**
     * Releases the warehouse and returns the next process in the queue.
     * @return the process instance id of the next process in the queue, or null if the queue is empty.
     */
    public String releaseHBW() {
        inUse.set(false);
        return waitingProcesses.poll();
    }

    /**
     * Adds a process instance id to the queue of waiting processes.
     * @param processInstanceId the id of the process to add to the queue.
     */
    public synchronized void addToQueue(String processInstanceId) {
        waitingProcesses.add(processInstanceId);
    }

}