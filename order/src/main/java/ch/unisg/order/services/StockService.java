package ch.unisg.order.services;


import ch.unisg.order.domain.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {


    private final Stock stock;


    public boolean checkStock(String color) {
        System.out.println("Checking stock for color: " + color);
        System.out.println("Stock: " + stock.getLatestStatus());
        return stock.getLatestStatus().containsValue(color);
    }

}
