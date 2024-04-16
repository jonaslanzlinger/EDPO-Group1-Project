package ch.unisg.monitoring.domain;

import ch.unisg.monitoring.kafka.dto.MonitorUpdateDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class MonitoringStore {

    private final AtomicReference<Map<String, List<MonitorUpdateDto>>> messageList = new AtomicReference<>(new HashMap<>() {
    });

    public void addMessage(MonitorUpdateDto monitorUpdateDto) {
        if (messageList.get().containsKey(monitorUpdateDto.getOrderId())) {
            messageList.get().get(monitorUpdateDto.getOrderId()).add(monitorUpdateDto);
        } else {
            List<MonitorUpdateDto> newList = new ArrayList<>();
            newList.add(monitorUpdateDto);
            messageList.get().put(monitorUpdateDto.getOrderId(), newList);
        }
    }

    public Map<String, List<MonitorUpdateDto>> getAllMessages() {
        return messageList.get();
    }

    public List<MonitorUpdateDto> getMessages(String orderId) {
        if(!messageList.get().containsKey(orderId)) {
            return new ArrayList<>();
        }
        return messageList.get().get(orderId);
    }

    public boolean containsOrder(String orderId) {
        return messageList.get().containsKey(orderId);
    }

    public boolean isEmpty() {
        return messageList.get().isEmpty();
    }
}
