package ch.unisg.monitoring.domain;

import ch.unisg.monitoring.kafka.dto.MonitorUpdateDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This is a service class for the MonitoringStore.
 * It uses Spring's @Service annotation to indicate that it is a service class.
 */
@Service
public class MonitoringStore {

    private final AtomicReference<Map<String, List<MonitorUpdateDto>>> messageList = new AtomicReference<>(new HashMap<>() {
    });

    /**
     * Add a message to the MonitoringStore.
     * If the order does not exist in the MonitoringStore, a new entry is created.
     *
     * @param monitorUpdateDto The message to add to the MonitoringStore.
     */
    public void addMessage(MonitorUpdateDto monitorUpdateDto) {
        if (messageList.get().containsKey(monitorUpdateDto.getOrderId())) {
            messageList.get().get(monitorUpdateDto.getOrderId()).add(monitorUpdateDto);
        } else {
            List<MonitorUpdateDto> newList = new ArrayList<>();
            newList.add(monitorUpdateDto);
            messageList.get().put(monitorUpdateDto.getOrderId(), newList);
        }
    }

    /**
     * Get all messages from the MonitoringStore.
     *
     * @return A map of all messages in the MonitoringStore.
     */
    public Map<String, List<MonitorUpdateDto>> getAllMessages() {
        return messageList.get();
    }

    /**
     * Get all messages for a specific order from the MonitoringStore.
     *
     * @param orderId The ID of the order to get messages for.
     * @return A list of messages for the specified order.
     */
    public List<MonitorUpdateDto> getMessages(String orderId) {
        if(!messageList.get().containsKey(orderId)) {
            return new ArrayList<>();
        }
        return messageList.get().get(orderId);
    }

    /**
     * Check if the MonitoringStore contains an entry for a specific orderId.
     *
     * @param orderId The ID of the order to check for.
     * @return True if the order exists in the MonitoringStore, false otherwise.
     */
    public boolean containsOrder(String orderId) {
        return messageList.get().containsKey(orderId);
    }

    /**
     * Check if the MonitoringStore is empty.
     *
     * @return True if the MonitoringStore is empty, false otherwise.
     */
    public boolean isEmpty() {
        return messageList.get().isEmpty();
    }
}
