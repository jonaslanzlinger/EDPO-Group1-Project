package ch.unisg.warehouse.camunda;

import ch.unisg.warehouse.domain.Order;
import ch.unisg.warehouse.domain.WarehouseService;
import ch.unisg.warehouse.kafka.dto.MonitorUpdateDto;
import ch.unisg.warehouse.kafka.producer.MonitorDataProducer;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import static ch.unisg.warehouse.utils.Utility.*;

/**
 * This is a service class that processes warehouse tasks.
 * It uses the CamundaService to interact with the Camunda engine.
 */
@Service
public class WarehouseProcessingService {
    private final String serviceName = "warehouse";
    private enum MonitorStatus {success, failed}
    private enum MonitorType {Event, Command}

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
    @JobWorker(type = "checkGoods", name = "checkGoodsProcessor",  autoComplete = false)
    public void checkGoods(final ActivatedJob job) {
        Order order = getOrder(job);

        String orderColor = order.getOrderColor();

        String productId = warehouseService.getProductSlot(orderColor);

        logInfo("checkGoods", "Processing order: "
                + job.getProcessInstanceKey() + " - " + orderColor);

        String orderId = order.getOrderId();
        if (productId == null) {
            logInfo("checkGoods", "Failed Order: "
                    + job.getProcessInstanceKey() + " - " + orderColor);
            camundaMessageSenderService.throwErrorCommand("GoodsNotAvailable",
                    String.format("No %s goods available", orderColor), job.getKey());
            monitorSuccessMessage(orderId, "checkGoods");
        } else {
            logInfo("checkGoods", "Complete order: "
                    + job.getProcessInstanceKey() + " - " + orderColor);
            camundaMessageSenderService.sendCompleteCommand(job.getKey(),
                    String.format("{\"productSlot\":\"%s\"}", productId));
            monitorFailedMessage(orderId, "checkGoods");
        }
    }


    /**
     * This method checks if the goods are available in the warehouse.
     * If the goods are not available, it throws an error command to the Camunda engine.
     * If the goods are available, it sends a complete command to the Camunda engine.
     *
     * @param job The job that contains the details of the order.
     */
    @JobWorker(type = "checkGoodsAvailable", name = "checkGoodsAvailableProcessor",  autoComplete = false)
    public void checkGoodsAvailable(final ActivatedJob job) {
        Order order = getOrder(job);
        String orderColor = order.getOrderColor();

        boolean isAvailable = warehouseService.checkProduct(orderColor);

        logInfo("checkGoodsAvailable", "Processing order: "
                + job.getProcessInstanceKey() + " - " + orderColor);

        String orderId = order.getOrderId();

        if (!isAvailable) {
            logInfo("checkGoodsAvailable", "Failed Order: "
                    + job.getProcessInstanceKey() + " - " + orderColor);
            camundaMessageSenderService.throwErrorCommand("GoodsNotAvailable",
                    String.format("No %s goods available", orderColor), job.getKey());
            monitorFailedMessage(orderId, "checkGoodsAvailable");
        } else {
            logInfo("checkGoodsAvailable", "Complete order: "
                    + job.getProcessInstanceKey() + " - " + orderColor);
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), job.getVariables());
            monitorSuccessMessage(orderId, "checkGoodsAvailable");
        }
    }

    /**
     * This method checks the status of the warehouse.
     * If the warehouse is in use, it sends a complete command to the Camunda engine with the available status as false.
     * If the warehouse is not in use, it sends a complete command to the Camunda engine with the available status as true.
     *
     * @param job The job that contains the details of the order.
     */
    @JobWorker(type = "checkHBW", name = "checkHBWProcessor",  autoComplete = false)
    public void checkHBWStatus(final ActivatedJob job) {
        Order order = getOrder(job);
        String orderId = order.getOrderId();
        boolean inUse = warehouseService.isInUse();

        logInfo("checkHBWStatus", "Checking HBW status");

        if (inUse) {
            logInfo("checkHBWStatus", "HBW is in use");
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), "{\"available\":\"false\"}");
            warehouseService.addToQueue(orderId);
            monitorFailedMessage(orderId, "checkHBW");
        } else {
            logInfo("checkHBWStatus", "HBW is not in use");
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), "{\"available\":\"true\"}");
            monitorSuccessMessage(orderId, "checkHBW");
        }
    }

    /**
     * This method tries to lock the warehouse.
     * If the warehouse is successfully locked, it sends a complete command to the Camunda engine with the locked status as true.
     * If the warehouse is already in use, it sends a complete command to the Camunda engine with the locked status as false.
     *
     * @param job The job that contains the details of the order.
     */
    @JobWorker(type = "lockHBW", name = "lockHBWProcessor",  autoComplete = false)
    public void lockHBW(final ActivatedJob job) {
        Order order = getOrder(job);
        String orderId = order.getOrderId();
        boolean success = warehouseService.setInUse();

        logInfo("lockHBW", "Locking HBW");


        if (success) {
            logInfo("lockHBW", "HBW locked");
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), "{\"locked\":\"true\"}");
            monitorSuccessMessage(orderId, "lockHBW");
        } else {
            logInfo("lockHBW", "HBW already in use");
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), "{\"locked\":\"false\"}");
            warehouseService.addToQueue(orderId);
            monitorFailedMessage(orderId, "lockHBW");
        }
    }

    /**
     * This method frees the warehouse.
     * After freeing the warehouse, it sends a complete command to the Camunda engine with the available status as true.
     * If there is a next process in the queue, it sends a message command to the Camunda engine with the available status as true.
     *
     * @param job The job that contains the details of the order.
     */
    @JobWorker(type = "freeHBW", name = "freeHBWProcessor",  autoComplete = false)
    public void freeHBW(final ActivatedJob job) {
        logInfo("freeHBW", "Freeing HBW");
        String nextProcess = warehouseService.releaseHBW();
        logInfo("freeHBW", "HBW freed");


        camundaMessageSenderService.sendCompleteCommand(job.getKey(), "{\"available\":\"true\"}");
        monitorSuccessMessage(getOrder(job).getOrderId(), "freeHBW");
        if (nextProcess != null) {
            logInfo("freeHBW", "Next process: " + nextProcess);
            camundaMessageSenderService.sendMessageCommand(
                    "HBWAvailable",
                    nextProcess,
                    "{\"available\":\"true\"}"
            );
        }
    }

    @JobWorker(type = "positionHBW", name = "positionHBWProcessor",  autoComplete = false)
    public void positionHBW(final ActivatedJob job) {
        logInfo("positionHBW", "Positioning HBW");
        camundaMessageSenderService.sendCompleteCommand(job.getKey(), job.getVariables());
        monitorSuccessMessage(getOrder(job).getOrderId(), "positionHBW");
        logInfo("positionHBW", "HBW positioned");
    }

    /**
     * This method unloads a product from the warehouse.
     * After unloading the product, it sends a complete command to the Camunda engine.
     *
     * @param job The job that contains the details of the order.
     */
    @JobWorker(type = "unloadProduct", name = "unloadProductProcessor",  autoComplete = false)
    public void unloadProduct(final ActivatedJob job) {
        logInfo("unloadProduct", "Unloading product");
        sleep(5000);
        String productSlot = job.getVariablesAsMap().get("productSlot").toString();
        warehouseService.getProduct(productSlot);
        camundaMessageSenderService.sendCompleteCommand(job.getKey(), job.getVariables());
        monitorSuccessMessage(getOrder(job).getOrderId(), "unloadProduct");
        logInfo("unloadProduct", "Unloaded product");
    }

    @JobWorker(type = "adjustStock", name = "adjustStockProcessor",  autoComplete = false)
    public void adjustStock(final ActivatedJob job) {
        logInfo("adjustStock", "Adjusting stock");

        Order order = getOrder(job);
        String orderColor = order.getOrderColor();

        String productSlot = getFromMap(job.getVariablesAsMap(), "productSlot", String.class);
        warehouseService.adjustStock(orderColor, productSlot);
        camundaMessageSenderService.sendCompleteCommand(job.getKey(), job.getVariables());
        monitorSuccessMessage(order.getOrderId(), "adjustStock");
        logInfo("adjustStock", "Stock adjusted");
    }

    private void monitorSuccessMessage(String orderId, String method) {
        monitorMessage(orderId, method, MonitorStatus.success.name());
    }

    private void monitorFailedMessage(String orderId, String method) {
        monitorMessage(orderId, method, MonitorStatus.failed.name());
    }

    private void monitorMessage(String orderId, String method, String status) {
        monitorDataProducer.sendMessage(
                new MonitorUpdateDto().builder()
                        .orderId(orderId)
                        .type(MonitorType.Event.name())
                        .method(method)
                        .status(status)
                        .service(serviceName)
                        .build());
    }

}