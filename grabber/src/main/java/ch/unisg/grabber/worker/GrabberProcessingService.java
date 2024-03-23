package ch.unisg.grabber.worker;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GrabberProcessingService {

    private final ZeebeClient zeebeClient;

    @Autowired
    public GrabberProcessingService(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @ZeebeWorker(type = "grabGoods", name = "grabGoodsProcessor")
    public void processOrder(final ActivatedJob job) {
        String orderDetails = job.getVariablesAsMap().get("orderDetails").toString();

        System.out.println("Processing order: " + orderDetails);

//        String resultVariable = "{\"result\": \"Success\"}";

        if (Math.random() < 0.5){
            System.out.println("New Complete Command: process instance" + job.getProcessInstanceKey() + " with key " + job.getKey() + " orderDetails: " + orderDetails);
            zeebeClient.newCompleteCommand(job.getKey())
                    .variables(orderDetails)
                    .send()
                    .join(); // Synchronous completion, remove join() for asynchronous
        } else {
            System.out.println("Wait for timeout: process instance" + job.getProcessInstanceKey() + " with key " + job.getKey() + " orderDetails: " + orderDetails);
        }
    }
}