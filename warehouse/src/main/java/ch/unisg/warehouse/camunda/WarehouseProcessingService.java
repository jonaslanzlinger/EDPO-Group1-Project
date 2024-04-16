package ch.unisg.warehouse.camunda;

import ch.unisg.warehouse.domain.WarehouseService;
import ch.unisg.warehouse.kafka.dto.MonitorUpdateDto;
import ch.unisg.warehouse.kafka.producer.MonitorDataProducer;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static ch.unisg.warehouse.utils.Utility.getFromMap;
import static ch.unisg.warehouse.utils.Utility.logInfo;

/**
 * This is a service class that processes warehouse tasks.
 * It uses the CamundaService to interact with the Camunda engine.
 */
@Service
public class WarehouseProcessingService {

    // The CamundaService instance used to send commands to the Camunda engine
    private final CamundaService camundaMessageSenderService;

    private final WarehouseService warehouseService;

    private final MonitorDataProducer monitorDataProducer;


    @Autowired
    public WarehouseProcessingService(CamundaService camundaMessageSenderService, WarehouseService warehouseService, MonitorDataProducer monitorDataProducer) {
        this.camundaMessageSenderService = camundaMessageSenderService;
        this.warehouseService = warehouseService;
        this.monitorDataProducer = monitorDataProducer;
    }

    /**
     * This method checks the goods in the warehouse.
     * If the goods are not available, it throws an error command to the Camunda engine.
     * If the goods are available, it sends a complete command to the Camunda engine.
     * It also sends the product slot to the Camunda engine.
     *
     * @param job The job that contains the details of the order.
     */
    @ZeebeWorker(type = "checkGoods", name = "checkGoodsProcessor")
    public void checkGoods(final ActivatedJob job) {
        Map<String, Object> order = getFromMap(job.getVariablesAsMap(), "order", Map.class);
        String orderColor = getFromMap(order, "orderColor", String.class);
        String productId = warehouseService.getProductSlot(orderColor);

        logInfo("checkGoods", "Processing order: "
                + job.getProcessInstanceKey() + " - " + orderColor);

        String orderId = getFromMap(order, "orderId", String.class);
        if (productId == null) {
            logInfo("checkGoods", "Failed Order: "
                    + job.getProcessInstanceKey() + " - " + orderColor);
            camundaMessageSenderService.throwErrorCommand("GoodsNotAvailable",
                    String.format("No %s goods available", orderColor), job.getKey());
            monitorDataProducer.sendMessage(
                    new MonitorUpdateDto().builder()
                            .orderId(orderId)
                            .type("Event")
                            .method("checkGoods")
                            .status("failed")
                            .service("warehouse")
                            .build());
        } else {
            logInfo("checkGoods", "Complete order: "
                    + job.getProcessInstanceKey() + " - " + orderColor);
            camundaMessageSenderService.sendCompleteCommand(job.getKey(),
                    String.format("{\"productSlot\":\"%s\"}", productId));
            monitorDataProducer.sendMessage(
                    new MonitorUpdateDto().builder()
                            .orderId(orderId)
                            .type("Event")
                            .method("checkGoods")
                            .status("success")
                            .service("warehouse")
                            .build());
        }
    }


    /**
     * This method checks if the goods are available in the warehouse.
     * If the goods are not available, it throws an error command to the Camunda engine.
     * If the goods are available, it sends a complete command to the Camunda engine.
     *
     * @param job The job that contains the details of the order.
     */
    @ZeebeWorker(type = "checkGoodsAvailable", name = "checkGoodsAvailableProcessor")
    public void checkGoodsAvailable(final ActivatedJob job) {
        Map<String, Object> order = getFromMap(job.getVariablesAsMap(), "order", Map.class);
        String orderColor = getFromMap(order, "orderColor", String.class);
        boolean isAvailable = warehouseService.checkProduct(orderColor);

        logInfo("checkGoodsAvailable", "Processing order: "
                + job.getProcessInstanceKey() + " - " + orderColor);

        String orderId = getFromMap(order, "orderId", String.class);

        if (!isAvailable) {
            logInfo("checkGoodsAvailable", "Failed Order: "
                    + job.getProcessInstanceKey() + " - " + orderColor);
            camundaMessageSenderService.throwErrorCommand("GoodsNotAvailable",
                    String.format("No %s goods available", orderColor), job.getKey());
            monitorDataProducer.sendMessage(
                    new MonitorUpdateDto().builder()
                            .orderId(orderId)
                            .type("Event")
                            .method("checkGoodsAvailable")
                            .status("failed")
                            .service("warehouse")
                            .build());
        } else {
            logInfo("checkGoodsAvailable", "Complete order: "
                    + job.getProcessInstanceKey() + " - " + orderColor);
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), job.getVariables());
            monitorDataProducer.sendMessage(
                    new MonitorUpdateDto().builder()
                            .orderId(orderId)
                            .type("Event")
                            .method("checkGoodsAvailable")
                            .status("success")
                            .service("warehouse")
                            .build());
        }
    }

    /**
     * This method checks the status of the warehouse.
     * If the warehouse is in use, it sends a complete command to the Camunda engine with the available status as false.
     * If the warehouse is not in use, it sends a complete command to the Camunda engine with the available status as true.
     *
     * @param job The job that contains the details of the order.
     */
    @ZeebeWorker(type = "checkHBW", name = "checkHBWProcessor")
    public void checkHBWStatus(final ActivatedJob job) {
        Map<String, Object> order = getFromMap(job.getVariablesAsMap(), "order", Map.class);
        String orderId = getFromMap(order, "orderId", String.class);
        boolean inUse = warehouseService.isInUse();

        logInfo("checkHBWStatus", "Checking HBW status");

        if (inUse) {
            logInfo("checkHBWStatus", "HBW is in use");
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), "{\"available\":\"false\"}");
            warehouseService.addToQueue(orderId);
            monitorDataProducer.sendMessage(
                    new MonitorUpdateDto().builder()
                            .orderId(orderId)
                            .type("Event")
                            .method("checkHBW")
                            .status("failed")
                            .service("warehouse")
                            .build());
        } else {
            logInfo("checkHBWStatus", "HBW is not in use");
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), "{\"available\":\"true\"}");
            monitorDataProducer.sendMessage(
                    new MonitorUpdateDto().builder()
                            .orderId(orderId)
                            .type("Event")
                            .method("checkHBW")
                            .status("success")
                            .service("warehouse")
                            .build());
        }
    }

    /**
     * This method tries to lock the warehouse.
     * If the warehouse is successfully locked, it sends a complete command to the Camunda engine with the locked status as true.
     * If the warehouse is already in use, it sends a complete command to the Camunda engine with the locked status as false.
     *
     * @param job The job that contains the details of the order.
     */
    @ZeebeWorker(type = "lockHBW", name = "lockHBWProcessor")
    public void lockHBW(final ActivatedJob job) {
        Map<String, Object> order = getFromMap(job.getVariablesAsMap(), "order", Map.class);
        String orderId = getFromMap(order, "orderId", String.class);
        boolean success = warehouseService.setInUse();

        logInfo("lockHBW", "Locking HBW");


        if (success) {
            logInfo("lockHBW", "HBW locked");
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), "{\"locked\":\"true\"}");
            monitorDataProducer.sendMessage(
                    new MonitorUpdateDto().builder()
                            .orderId(orderId)
                            .type("Event")
                            .method("lockHBW")
                            .status("success")
                            .service("warehouse")
                            .build());
        } else {
            logInfo("lockHBW", "HBW already in use");
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), "{\"locked\":\"false\"}");
            warehouseService.addToQueue(orderId);
            monitorDataProducer.sendMessage(
                    new MonitorUpdateDto().builder()
                            .orderId(orderId)
                            .type("Event")
                            .method("lockHBW")
                            .status("failed")
                            .service("warehouse")
                            .build());
        }
    }

    /**
     * This method frees the warehouse.
     * After freeing the warehouse, it sends a complete command to the Camunda engine with the available status as true.
     * If there is a next process in the queue, it sends a message command to the Camunda engine with the available status as true.
     *
     * @param job The job that contains the details of the order.
     */
    @ZeebeWorker(type = "freeHBW", name = "freeHBWProcessor")
    public void freeHBW(final ActivatedJob job) {
        logInfo("freeHBW", "Freeing HBW");
        String nextProcess = warehouseService.releaseHBW();
        logInfo("freeHBW", "HBW freed");


        camundaMessageSenderService.sendCompleteCommand(job.getKey(), "{\"available\":\"true\"}");
        if (nextProcess != null) {
            logInfo("freeHBW", "Next process: " + nextProcess);
            camundaMessageSenderService.sendMessageCommand(
                    "HBWAvailable",
                    nextProcess,
                    "{\"available\":\"true\"}"
            );
        }
    }

    @ZeebeWorker(type = "positionHBW", name = "positionHBWProcessor")
    public void positionHBW(final ActivatedJob job) {
        logInfo("positionHBW", "Positioning HBW");
        camundaMessageSenderService.sendCompleteCommand(job.getKey(), job.getVariables());
        logInfo("positionHBW", "HBW positioned");
    }

    /**
     * This method unloads a product from the warehouse.
     * After unloading the product, it sends a complete command to the Camunda engine.
     *
     * @param job The job that contains the details of the order.
     */
    @ZeebeWorker(type = "unloadProduct", name = "unloadProductProcessor")
    public void unloadProduct(final ActivatedJob job) {
        logInfo("unloadProduct", "Unloading product");

        String productSlot = job.getVariablesAsMap().get("productSlot").toString();
        warehouseService.getProduct(productSlot);
        camundaMessageSenderService.sendCompleteCommand(job.getKey(), job.getVariables());
        logInfo("unloadProduct", "Unloaded product");
    }

    @ZeebeWorker(type = "adjustStock", name = "adjustStockProcessor")
    public void adjustStock(final ActivatedJob job) {
        logInfo("adjustStock", "Adjusting stock");

        Map<String, Object> order = getFromMap(job.getVariablesAsMap(), "order", Map.class);
        String orderColor = getFromMap(order, "orderColor", String.class);

        String productSlot = getFromMap(job.getVariablesAsMap(), "productSlot", String.class);
        warehouseService.adjustStock(orderColor, productSlot);
        camundaMessageSenderService.sendCompleteCommand(job.getKey(), job.getVariables());
        logInfo("adjustStock", "Stock adjusted");
    }

}