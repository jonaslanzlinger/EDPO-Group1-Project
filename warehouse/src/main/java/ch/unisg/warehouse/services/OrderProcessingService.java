package ch.unisg.warehouse.services;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessingService {

    @ZeebeWorker(type = "checkGoods", name = "checkGoodsProcessor")
    public void processOrder(final ActivatedJob job) {
        // Extract variables from the job
        String orderDetails = job.getVariablesAsMap().get("orderDetails").toString();

        // Implement your business logic here
        System.out.println("Processing order: " + orderDetails);

        // Optionally update process variables or complete the job with new variables
    }
}
