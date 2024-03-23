package ch.unisg.warehouse.services;

import ch.unisg.warehouse.utils.WorkflowLogger;
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


        if(orderDetails.contains("red")) {
            // Throw an exception
            System.out.println("Throwing error: GoodsNotAvailable for process instance " + job.getProcessInstanceKey() + " with key " + job.getKey());
            zeebeClient.newThrowErrorCommand(job.getKey())
                    .errorCode("GoodsNotAvailable")
                    .errorMessage("The order contains red goods")
                    .send()
                    .join(); // Synchronous throwing, remove join() for asynchronous
        } else {
            // Complete the job
            System.out.println("Job success: checkGoods for process instance " + job.getProcessInstanceKey() + " with key " + job.getKey());
            zeebeClient.newCompleteCommand(job.getKey())
                    .variables(resultVariable)
                    .send()
                    .join(); // Synchronous completion, remove join() for asynchronous
            // Throw an exception
        }
    }
}

