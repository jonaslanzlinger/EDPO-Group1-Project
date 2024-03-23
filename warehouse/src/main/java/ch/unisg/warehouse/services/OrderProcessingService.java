package ch.unisg.warehouse.services;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderProcessingService {
    private final CamundaMessageSenderService startWarehouseProcessService;

    @Autowired
    public OrderProcessingService(CamundaMessageSenderService startWarehouseProcessService) {
        this.startWarehouseProcessService = startWarehouseProcessService;
    }

    @ZeebeWorker(type = "checkGoods", name = "checkGoodsProcessor")
    public void processOrder(final ActivatedJob job) {
        // Extract variables from the job
        String orderDetails = job.getVariablesAsMap().get("orderDetails").toString();
        long mainProcessKey = job.getKey();
        String fullDetailsJson = "{\"orderDetails\":\"" + orderDetails + "\"},\"mainProcessKey\":\"" + mainProcessKey + "\"}";
        // Implement your business logic here
        System.out.println("Processing order: " + orderDetails);
        startWarehouseProcessService.sendMessage("Msg_CheckGoodsReceived",UUID.randomUUID().toString(), fullDetailsJson);
        // Optionally update process variables or complete the job with new variables
    }
}

