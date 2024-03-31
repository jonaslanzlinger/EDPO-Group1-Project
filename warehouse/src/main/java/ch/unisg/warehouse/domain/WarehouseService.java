package ch.unisg.warehouse.domain;


import ch.unisg.warehouse.kafka.dto.StockUpdateDto;
import ch.unisg.warehouse.kafka.dto.WarehouseUpdateDto;
import ch.unisg.warehouse.kafka.producer.MessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WarehouseService {

    private final WarehouseStatusService warehouseStatusService;

    private final MessageProducer messageProducer;


    public void updateWarehouse(WarehouseUpdateDto message) {
        // TODO: DO MORE STUFF HERE
        HBW_1 hbw_1 = message.getData();

        // Update the warehouse status with the data from the message
        warehouseStatusService.updateWarehouseStatus(hbw_1);

        // build StockUpdateDto
        StockUpdateDto stockUpdateDto = StockUpdateDto.builder()
                .type(message.getType())
                .id(message.getId())
                .source(message.getSource())
                .time(message.getTime())
                .data(message.getData().getCurrent_stock())
                .datacontenttype(message.getDatacontenttype())
                .specversion(message.getSpecversion())
                .build();

        // send the updated status to the Kafka topic "warehouse"
        messageProducer.send(stockUpdateDto);
    }


    public String getProduct(String color) {
        // Get the latest status of the warehouse
        HBW_1 hbw_1 = warehouseStatusService.getLatestStatus();

        // in case the warehouse has not been initialized yet
        if (hbw_1 == null) {
            return null;
        }

        String productId = hbw_1.getCurrent_stock().entrySet().stream()
                .filter(entry -> entry.getValue().equals(color))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (productId == null) {
            return null;
        }

        // Remove the product from the warehouse
        hbw_1.getCurrent_stock().put(productId, "");

        // Update the warehouse status
        warehouseStatusService.updateWarehouseStatus(hbw_1);

        return productId;
    }


    public String getStock() {
        // Get the latest status of the warehouse
        HBW_1 hbw_1 = warehouseStatusService.getLatestStatus();
        return hbw_1.getCurrent_stock().toString();
    }


}
