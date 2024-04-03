package ch.unisg.warehouse.camunda;

import ch.unisg.warehouse.domain.WarehouseService;
import ch.unisg.warehouse.utils.WorkflowLogger;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static ch.unisg.warehouse.utils.Utility.getFromMap;

/**
 * This is a service class that processes warehouse tasks.
 * It uses the CamundaService to interact with the Camunda engine.
 */
@Service
@Slf4j
public class WarehouseProcessingService {

    // The CamundaService instance used to send commands to the Camunda engine
    private final CamundaService camundaMessageSenderService;

    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseProcessingService(CamundaService camundaMessageSenderService, WarehouseService warehouseService) {
        this.camundaMessageSenderService = camundaMessageSenderService;
        this.warehouseService = warehouseService;
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

        WorkflowLogger.info(log, "checkGoods","Processing order: "
                + job.getProcessInstanceKey() + " - " + orderColor);


        if (productId == null) {
            WorkflowLogger.info(log, "checkGoods", "Failed Order: "
                    + job.getProcessInstanceKey()+ " - " + orderColor);
            camundaMessageSenderService.throwErrorCommand("GoodsNotAvailable",
                    String.format("No %s goods available", orderColor), job.getKey());
        } else {
            WorkflowLogger.info(log, "checkGoods", "Complete order: "
                    + job.getProcessInstanceKey()+ " - " + orderColor);
            camundaMessageSenderService.sendCompleteCommand(job.getKey(),
                    String.format("{\"productSlot\":\"%s\"}", productId));
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

        WorkflowLogger.info(log, "checkGoodsAvailable","Processing order: "
                + job.getProcessInstanceKey() + " - " + orderColor);

        if (!isAvailable) {
            WorkflowLogger.info(log, "checkGoodsAvailable", "Failed Order: "
                    + job.getProcessInstanceKey()+ " - " + orderColor);
            camundaMessageSenderService.throwErrorCommand("GoodsNotAvailable",
                    String.format("No %s goods available", orderColor), job.getKey());
        } else {
            WorkflowLogger.info(log, "checkGoodsAvailable", "Complete order: "
                    + job.getProcessInstanceKey()+ " - " + orderColor);
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), job.getVariables());
        }
    }

    @ZeebeWorker(type = "checkHBW", name = "checkHBWProcessor")
    public void checkHBWStatus(final ActivatedJob job) {
        Map<String, Object> order = getFromMap(job.getVariablesAsMap(), "order", Map.class);
        String orderId = getFromMap(order, "orderId", String.class);
        boolean inUse = warehouseService.isInUse();

        WorkflowLogger.info(log, "checkHBWStatus", "Checking HBW status");

        if (inUse) {
            WorkflowLogger.info(log, "checkHBWStatus", "HBW is in use");
            camundaMessageSenderService.sendCompleteCommand(job.getKey(),"{\"available\":\"false\"}");
            warehouseService.addToQueue(orderId);
        } else {
            WorkflowLogger.info(log, "checkHBWStatus", "HBW is not in use");
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), "{\"available\":\"true\"}");
        }
    }

    @ZeebeWorker(type = "lockHBW", name = "lockHBWProcessor")
    public void lockHBW(final ActivatedJob job) {
        Map<String, Object> order = getFromMap(job.getVariablesAsMap(), "order", Map.class);
        String orderId = getFromMap(order, "orderId", String.class);
        boolean success = warehouseService.setInUse();

        WorkflowLogger.info(log, "lockHBW", "Locking HBW");

        if (success) {
            WorkflowLogger.info(log, "lockHBW", "HBW locked");
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), "{\"locked\":\"true\"}");
        } else {
            WorkflowLogger.info(log, "lockHBW", "HBW already in use");
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), "{\"locked\":\"false\"}");
            warehouseService.addToQueue(orderId);
        }
    }

    @ZeebeWorker(type = "freeHBW", name = "freeHBWProcessor")
    public void freeHBW(final ActivatedJob job) {

        WorkflowLogger.info(log, "freeHBW", "Freeing HBW");
        String nextProcess = warehouseService.releaseHBW();
        WorkflowLogger.info(log, "freeHBW", "HBW freed");

        camundaMessageSenderService.sendCompleteCommand(job.getKey(), "{\"available\":\"true\"}");
        if (nextProcess != null) {
            WorkflowLogger.info(log, "freeHBW", "Next process: " + nextProcess);
            camundaMessageSenderService.sendMessageCommand(
                    "HBWAvailable",
                    nextProcess,
                    "{\"available\":\"true\"}"
            );
        }
    }

    @ZeebeWorker(type = "unloadProduct", name = "unloadProductProcessor")
    public void unloadProduct(final ActivatedJob job) {
        WorkflowLogger.info(log, "unloadProduct", "Unloading product");
        String productSlot = job.getVariablesAsMap().get("productSlot").toString();
        warehouseService.getProduct(productSlot);
        camundaMessageSenderService.sendCompleteCommand(job.getKey(), job.getVariables());
        WorkflowLogger.info(log, "unloadProduct", "unloaded product");
    }
}