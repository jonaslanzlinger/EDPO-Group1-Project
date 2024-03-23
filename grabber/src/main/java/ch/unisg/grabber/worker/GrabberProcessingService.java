package ch.unisg.grabber.worker;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrabberProcessingService {

    private final ZeebeClient zeebeClient;

    @Autowired
    public GrabberProcessingService(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @ZeebeWorker(type = "grabGoods", name = "grabGoodsProcessor")
    public void processOrder(final ActivatedJob job) {
        // Extract variables from the job
        String orderDetails = job.getVariablesAsMap().get("orderDetails").toString();

        // Implement your business logic here
        System.out.println("Starting grabbing order: " + orderDetails);

        // Optionally update process variables or complete the job with new variables
        String resultVariable = "{\"result\": \"Success\"}";

        // Complete the job
        System.out.println("Job success: grabGoods for process instance" + job.getProcessInstanceKey() + " with key " + job.getKey());
        zeebeClient.newCompleteCommand(job.getKey())
                .variables(resultVariable)
                .send()
                .join(); // Synchronous completion, remove join() for asynchronous
        // Throw an exception
    }
}

