package ch.unisg.order.domain;


import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class Stock {

    private static Stock singleton;

    private Map<String,String> stock;

    public Stock(Map<String,String> stock) {
        this.stock = stock;
    }

    public static Stock getSingleton() {
        if (singleton == null) {
            singleton = new Stock(new HashMap<>());
        }
        return singleton;
    }

    private Map<String,String> getStock() {
        return stock;
    }

    public void updatePosition(String pos, String col) {
        stock.put(pos, col);
    }


}
