package ch.unisg.warehouse.domain;

import ch.unisg.warehouse.camunda.CamundaService;
import ch.unisg.warehouse.kafka.dto.StockUpdateDto;
import ch.unisg.warehouse.kafka.dto.WarehouseUpdateDto;
import ch.unisg.warehouse.kafka.producer.StockUpdateProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static ch.unisg.warehouse.utils.Utility.logInfo;

/**
 * This is a service class that manages the warehouse.
 * It uses the WarehouseStatusService to interact with the warehouse status and the StockUpdateProducer to send messages.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WarehouseService {

    private final WarehouseStatusService warehouseStatusService;
    private final StockUpdateProducer messageProducer;
    private final CamundaService camundaService;

    /**
     * Updates the warehouse status with the data from the message and sends the updated status to the Kafka topic "warehouse".
     * @param message The message containing the new warehouse status.
     */
    public void updateWarehouse(WarehouseUpdateDto message) {
        updateWarehouse(message.getData());
    }

    /**
     * Updates the warehouse status with the data from the HBW_1 object and sends the updated status to the Kafka topic "warehouse".
     * Status is only propagated to order service if stock differs from prev stock
     * @param hbw_1 The HBW_1 object containing the new warehouse status.
     */
    public void updateWarehouse(HBW_1 hbw_1) {
        HBW_1 prevStatus = warehouseStatusService.getLatestStatus();
        warehouseStatusService.updateWarehouseStatus(hbw_1);
        if (prevStatus.getCurrent_stock() == null ||!prevStatus.getCurrent_stock().equals(hbw_1.getCurrent_stock())) {
            StockUpdateDto stockUpdateDto = StockUpdateDto.builder()
                    .data(hbw_1.getCurrent_stock())
                    .build();
            messageProducer.sendMessage(stockUpdateDto);
        }
    }

    /**
     * Retrieves a product of a specific color from the warehouse.
     * @param color The color of the product to retrieve.
     * @return The product id of the retrieved product, or null if no product of the specified color is found.
     */
    public String getProduct(String color) throws URISyntaxException, InterruptedException, IOException {
        String productId = getProductSlot(color);
        if (productId == null) {
            return null;
        }
        HBW_1 hbw_1 = warehouseStatusService.getLatestStatus();
        if (hbw_1 == null) {
            return null;
        }

        String url = "http://host.docker.internal:5001/process/unload_from_hbw_container?color=" + color;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {
            // Check response status code or handle response
            System.out.println("Response status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            // Call releaseHBW on successful completion of request
            String nextProcess = warehouseStatusService.releaseHBW();
            if (nextProcess != null) {
                logInfo("freeHBW", "Next process: " + nextProcess);
                camundaService.sendMessageCommand(
                        "HBWAvailable",
                        nextProcess,
                        "{\"available\":\"true\"}"
                );
            }
        }).exceptionally(ex -> {
            // Handle any exceptions here
            System.err.println("Request failed: " + ex.getMessage());
            return null;
        });
        // onComplete -> HBW release
        hbw_1.getCurrent_stock().put(productId, "");
        updateWarehouse(hbw_1);
        return productId;
    }

    /**
     * Retrieves the product slot of a product of a specific color from the warehouse.
     * @param color The color of the product to retrieve the slot for.
     * @return The product slot of the product, or null if no product of the specified color is found.
     */
    public String getProductSlot(String color) {
        HBW_1 hbw_1 = warehouseStatusService.getLatestStatus();
        if (hbw_1 == null) {
            return null;
        }
        return hbw_1.getCurrent_stock().entrySet().stream()
                .filter(entry -> entry.getValue().equals(color))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    /**
     * Checks if a product of a specific color is available in the warehouse.
     * @param color The color of the product to check for.
     * @return true if a product of the specified color is available, false otherwise.
     */
    public boolean checkProduct(String color) {
        String productId = getProductSlot(color);
        return productId != null;
    }

    /**
     * Retrieves the current stock of the warehouse.
     * @return A string representation of the current stock of the warehouse.
     */
    public String getStock() {
        HBW_1 hbw_1 = warehouseStatusService.getLatestStatus();
        if (hbw_1 == null) {
            return null;
        }
        return hbw_1.getCurrent_stock().toString();
    }

    /**
     * Tries to reserve the warehouse.
     * @return true if the warehouse was successfully reserved, false otherwise.
     */
    public boolean setInUse() {
        return warehouseStatusService.tryReserveHBW();
    }

    /**
     * Checks if the warehouse is currently in use.
     * @return true if the warehouse is in use, false otherwise.
     */
    public boolean isInUse() {
        return warehouseStatusService.isInUse();
    }

    /**
     * Releases the warehouse and returns the next process in the queue.
     * @return the process instance id of the next process in the queue, or null if the queue is empty.
     */
    public String releaseHBW() {
        return warehouseStatusService.releaseHBW();
    }

    /**
     * Adds a process instance id to the queue of waiting processes.
     * @param processInstanceId the id of the process to add to the queue.
     */
    public void addToQueue(String processInstanceId) {
        warehouseStatusService.addToQueue(processInstanceId);
    }

    public void adjustStock(String color, String productSlot) {
        HBW_1 hbw_1 = warehouseStatusService.getLatestStatus();
        hbw_1.getCurrent_stock().put(productSlot, color);
        updateWarehouse(hbw_1);
    }

    /**
     * Sends a GET request to the endpoint.
     * Once we receive an answer we know it has been calibrated.
     */
    public void positionHBW() throws URISyntaxException {
        String url = "http://host.docker.internal:5001/hbw/calibrate?machine=hbw_1";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            url = "http://host.docker.internal:8085/hbw/calibrate";
            request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException ex) {
                log.error("Error while calibrating HBW: " + e.getMessage());
            }
        }
        log.info("Response from calibration: " + response.body());
    }
}