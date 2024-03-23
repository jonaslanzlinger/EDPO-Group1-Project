package ch.unisg.warehouse.services;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarehouseProcessingService {

    private final ZeebeClient zeebeClient;

    @Autowired
    public WarehouseProcessingService(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @ZeebeWorker(type = "checkGoods", name = "checkGoodsProcessor")
    public void checkGoods(final ActivatedJob job) {
        String orderDetails = job.getVariablesAsMap().get("orderDetails").toString();

        System.out.println("Processing order: " + orderDetails);

        String variables = String.format("{\"orderDetails\": \"%s\"}", orderDetails);

        if (orderDetails.contains("red")) {
            System.out.println("New Throw Error Command: GoodsNotAvailable for process instance " + job.getProcessInstanceKey() + " with key " + job.getKey() + " orderDetails: " + orderDetails);
            zeebeClient.newThrowErrorCommand(job.getKey())
                    .errorCode("GoodsNotAvailable")
                    .errorMessage(String.format("No %s goods available", orderDetails))
                    .send()
                    .join(); // Synchronous throwing, remove join() for asynchronous
        } else {
            System.out.println("New Complete Command: process instance " + job.getProcessInstanceKey() + " with key " + job.getKey() + " orderDetails: " + orderDetails);
            zeebeClient.newCompleteCommand(job.getKey())
                    .variables(variables)
                    .send()
                    .join(); // Synchronous completion, remove join() for asynchronous
        }
    }
}

