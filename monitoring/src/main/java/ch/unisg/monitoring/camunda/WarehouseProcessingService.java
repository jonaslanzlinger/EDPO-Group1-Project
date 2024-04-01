package ch.unisg.monitoring.camunda;

import ch.unisg.monitoring.domain.WarehouseService;
import ch.unisg.monitoring.utils.WorkflowLogger;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    /**
     * Constructor for the WarehouseProcessingService class.
     * @param camundaMessageSenderService The CamundaService instance to be used for interactions with the Camunda engine.
     */
    @Autowired
    public WarehouseProcessingService(CamundaService camundaMessageSenderService, WarehouseService warehouseService) {
        this.camundaMessageSenderService = camundaMessageSenderService;
        this.warehouseService = warehouseService;
    }

    /**
     * This method checks the goods in the warehouse.
     * If the goods are not available, it throws an error command to the Camunda engine.
     * If the goods are available, it sends a complete command to the Camunda engine.
     * @param job The job that contains the details of the order.
     */
    @ZeebeWorker(type = "checkGoods", name = "checkGoodsProcessor")
    public void checkGoods(final ActivatedJob job) {
        Map<String, Object> orderVariables = (Map<String, Object>) job.getVariablesAsMap().get("order");
        // Now you can access individual properties within the 'order' object
        String orderColor = (String) orderVariables.get("orderColor");

        WorkflowLogger.info(log, "checkGoods","Processing order: " + job.getProcessInstanceKey() + " - " + orderColor);

        String productId = warehouseService.getProductSlot(orderColor);

        String productSlot = String.format("{\"productSlot\":\"%s\"}", productId);

        if (productId == null) {
            WorkflowLogger.info(log, "checkGoods", "Failed Order: " + job.getProcessInstanceKey()+ " - " + orderColor);
            camundaMessageSenderService.throwErrorCommand("GoodsNotAvailable",
                    String.format("No %s goods available", orderColor), job.getKey());
        } else {
            WorkflowLogger.info(log, "checkGoods", "Complete order: " + job.getProcessInstanceKey()+ " - " + orderColor);
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), productSlot);
        }
    }

    @ZeebeWorker(type = "checkGoodsAvailable", name = "checkGoodsAvailableProcessor")
    public void checkGoodsAvailable(final ActivatedJob job) {
        Map<String, Object> orderVariables = (Map<String, Object>) job.getVariablesAsMap().get("order");
        // Now you can access individual properties within the 'order' object
        String orderColor = (String) orderVariables.get("orderColor");

        WorkflowLogger.info(log, "checkGoodsAvailable","Processing order: " + job.getProcessInstanceKey() + " - " + orderColor);


        boolean isAvailable = warehouseService.checkProduct(orderColor);

        if (!isAvailable) {
            WorkflowLogger.info(log, "checkGoodsAvailable", "Failed Order: " + job.getProcessInstanceKey()+ " - " + orderColor);
            camundaMessageSenderService.throwErrorCommand("GoodsNotAvailable",
                    String.format("No %s goods available", orderColor), job.getKey());
        } else {
            WorkflowLogger.info(log, "checkGoodsAvailable", "Complete order: " + job.getProcessInstanceKey()+ " - " + orderColor);
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), job.getVariables());
        }
    }

    @ZeebeWorker(type = "checkHBW", name = "checkHBWProcessor")
    public void checkHBWStatus(final ActivatedJob job) {
        WorkflowLogger.info(log, "checkHBWStatus", "Checking HBW status");
        Map<String, Object> orderVariables = (Map<String, Object>) job.getVariablesAsMap().get("order");
        // Now you can access individual properties within the 'order' object
        String orderId = (String) orderVariables.get("orderId");
        boolean inUse = warehouseService.isInUse();
        String inUseJson = String.format("{\"available\":\"%s\"}", !inUse);

        if (inUse) {
            WorkflowLogger.info(log, "checkHBWStatus", "HBW is in use");
            camundaMessageSenderService.sendCompleteCommand(job.getKey(),inUseJson);
            warehouseService.addToQueue(orderId);
        } else {
            WorkflowLogger.info(log, "checkHBWStatus", "HBW is not in use");
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), inUseJson);
        }
    }

    @ZeebeWorker(type = "lockHBW", name = "lockHBWProcessor")
    public void lockHBW(final ActivatedJob job) {
        WorkflowLogger.info(log, "lockHBW", "Locking HBW");
        Map<String, Object> orderVariables = (Map<String, Object>) job.getVariablesAsMap().get("order");
        // Now you can access individual properties within the 'order' object
        String orderId = (String) orderVariables.get("orderId");

        boolean success = warehouseService.setInUse();
        String lockedJson = String.format("{\"locked\":\"%s\"}", success);

        if (success) {
            WorkflowLogger.info(log, "lockHBW", "HBW locked");
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), lockedJson);
        } else {
            WorkflowLogger.info(log, "lockHBW", "HBW already in use");
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), lockedJson);
            warehouseService.addToQueue(orderId);
        }
    }

    @ZeebeWorker(type = "freeHBW", name = "freeHBWProcessor")
    public void freeHBW(final ActivatedJob job) {
        WorkflowLogger.info(log, "freeHBW", "Freeing HBW");

        String nextProcess = warehouseService.releaseHBW();
        String freedJson = String.format("{\"available\":\"%s\"}", true);
        WorkflowLogger.info(log, "freeHBW", "HBW freed");
        camundaMessageSenderService.sendCompleteCommand(job.getKey(), freedJson);

        if (nextProcess != null) {
            WorkflowLogger.info(log, "freeHBW", "Next process: " + nextProcess);
            camundaMessageSenderService.sendMessageCommand("HBWAvailable",nextProcess,freedJson);
        }
    }

    @ZeebeWorker(type = "unloadProduct", name = "unloadProductProcessor")
    public void unloadProduct(final ActivatedJob job) {
        WorkflowLogger.info(log, "unloadProduct", "Unloading product");
        String productSlot = job.getVariablesAsMap().get("productSlot").toString();
        warehouseService.getProduct(productSlot);
        camundaMessageSenderService.sendCompleteCommand(job.getKey(), job.getVariables());
    }
}