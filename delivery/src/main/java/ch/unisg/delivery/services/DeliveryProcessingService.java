package ch.unisg.delivery.services;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryProcessingService {

    private final ZeebeClient zeebeClient;

    @Autowired
    public DeliveryProcessingService(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @ZeebeWorker(type = "deliverGoods", name = "deliverGoodsProcessor")
    public void deliverGoods(final ActivatedJob job) {
        String orderDetails = job.getVariablesAsMap().get("orderDetails").toString();

        System.out.println("Processing order: " + orderDetails);

        String variables = String.format("{\"orderDetails\": \"%s\"}", orderDetails);

        if (orderDetails.contains("blue")) {
            System.out.println("New Throw Error Command: DeliveryStationError for process instance " + job.getProcessInstanceKey() + " with key " + job.getKey() + " orderDetails: " + orderDetails);
            zeebeClient.newThrowErrorCommand(job.getKey())
                    .errorCode("DeliveryStationError")
                    .errorMessage(String.format("Orders with %s goods fail in delivery station", orderDetails))
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

