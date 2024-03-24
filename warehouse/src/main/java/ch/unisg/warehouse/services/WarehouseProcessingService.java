package ch.unisg.warehouse.services;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WarehouseProcessingService {

    private final CamundaService camundaMessageSenderService;

    @Autowired
    public WarehouseProcessingService(CamundaService camundaMessageSenderService) {
        this.camundaMessageSenderService = camundaMessageSenderService;
    }

    @ZeebeWorker(type = "checkGoods", name = "checkGoodsProcessor")
    public void checkGoods(final ActivatedJob job) {
        String orderDetails = job.getVariablesAsMap().get("orderDetails").toString();
        System.out.println("Processing order: " + orderDetails);
        String variables = String.format("{\"orderDetails\": \"%s\"}", orderDetails);

        // TODO: Remove hardcoded stuff here
        if (orderDetails.contains("red")) {
            System.out.println("New Throw Error Command: GoodsNotAvailable for process instance " + job.getProcessInstanceKey() + " with key " + job.getKey() + " orderDetails: " + orderDetails);
            camundaMessageSenderService.throwErrorCommand("GoodsNotAvailable", String.format("No %s goods available", orderDetails), job.getKey());

        } else {
            System.out.println("New Complete Command: process instance " + job.getProcessInstanceKey() + " with key " + job.getKey() + " orderDetails: " + orderDetails);
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), variables);
        }
    }
}

