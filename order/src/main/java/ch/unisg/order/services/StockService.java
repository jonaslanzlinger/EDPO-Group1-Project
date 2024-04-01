package ch.unisg.order.services;


import ch.unisg.order.domain.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class StockService {


    private final Stock stock;


    public boolean checkStock(String color) {
        return stock.getLatestStatus().containsValue(color);
    }

    public void removeColorFromStock(String color) {
        stock.removeColor(color);
    }

    public Map<String, String> getStock() {
        return stock.getLatestStatus();
    }

}
