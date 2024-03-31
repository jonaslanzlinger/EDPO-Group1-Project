package ch.unisg.order.domain;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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
}
