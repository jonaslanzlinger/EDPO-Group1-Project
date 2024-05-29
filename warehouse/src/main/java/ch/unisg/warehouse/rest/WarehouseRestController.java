package ch.unisg.warehouse.rest;

import ch.unisg.warehouse.domain.HBW_1;
import ch.unisg.warehouse.domain.WarehouseService;
import ch.unisg.warehouse.kafka.dto.WarehouseUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * This is a REST controller for the warehouse.
 * It provides endpoints for setting the warehouse status and getting the current stock.
 */
@RestController
@RequiredArgsConstructor
public class WarehouseRestController {

    private final WarehouseService warehouseService;

    /**
     * This endpoint sets the status of the warehouse.
     * It creates a new WarehouseUpdateDto with the new status and passes it to the WarehouseService.
     * @return A string indicating that the warehouse status has been updated.
     */
    @GetMapping("/setStatus")
    public String setStatus() {

        // This is deprecated and not needed anymore, because we get the current stock from Kafka by the
        // factory
        // "current_stock": {"0": "", "1": "", "2": "", "3": "", "4": "", "5": "", "6": "", "7": "", "8": ""}

        HBW_1 hbw_1 = HBW_1.builder()
                .current_stock(new HashMap<>(){
                    {
                        put("0", "blue");
                        put("1", "red");
                        put("2", "white");
                        put("3", "blue");
                        put("4", "red");
                        put("5", "white");
                        put("6", "blue");
                        put("7", "red");
                        put("8", "white");
                    }
                })
                .build();

        WarehouseUpdateDto dto = WarehouseUpdateDto.builder()
                .type("update")
                .id("1")
                .source("warehouse")
                .time("2021-09-01T12:00:00Z")
                .data(hbw_1)
                .datacontenttype("application/json")
                .specversion("1.0")
                .build();

        warehouseService.updateWarehouse(dto);

        return "Warehouse status updated";
    }
    /**
     * This endpoint retrieves the current stock of the warehouse.
     * It calls the getStock method of the WarehouseService and returns the result.
     * @return A string representation of the current stock of the warehouse.
     */
    @GetMapping("/stock")
    public String getStock() {
        return warehouseService.getStock();
    }
}