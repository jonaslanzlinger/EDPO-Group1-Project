package ch.unisg.warehouse.services;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessingService {

    private final ZeebeClient zeebeClient;

    @Autowired
    public OrderProcessingService(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @ZeebeWorker(type = "checkGoods", name = "checkGoodsProcessor")
    public void processOrder(final ActivatedJob job) {
        // Extract variables from the job
        String orderDetails = job.getVariablesAsMap().get("orderDetails").toString();

        // Implement your business logic here
        System.out.println("Processing order: " + orderDetails);

        // Optionally update process variables or complete the job with new variables
        String resultVariable = "{\"result\": \"Success\"}";

        // Complete the job
        zeebeClient.newCompleteCommand(job.getKey())
                .variables(resultVariable)
                .send()
                .join(); // Synchronous completion, remove join() for asynchronous
    }
}

