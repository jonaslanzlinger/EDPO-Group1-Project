package ch.unisg.order.domain;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This is a domain class for Stock.
 * It is a singleton class that stores the latest status of the warehouse.
 */
@Service
public class Stock {

    // The latest status of the warehouse
    private final AtomicReference<Map<String,String>> latestStatus = new AtomicReference<>(new HashMap<>(){});

    public void updateWarehouseStatus(Map<String,String> stock) {
        latestStatus.set(stock);
    }
    public Map<String,String> getLatestStatus() {
        return latestStatus.get();
    }

    public void removeColor(String color) {
        // get first key whose value is equal to color
        latestStatus.get().entrySet().stream()
                .filter(entry -> color.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst().ifPresent(key -> latestStatus.get().remove(key));
    }
}
