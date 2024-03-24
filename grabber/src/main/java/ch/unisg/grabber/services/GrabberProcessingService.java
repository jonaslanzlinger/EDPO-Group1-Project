package ch.unisg.grabber.services;

import ch.unisg.grabber.utils.WorkflowLogger;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is a service class for processing Grabber tasks.
 * It uses Spring's @Service annotation to indicate that it is a service class.
 * It uses Camunda's ZeebeClient to interact with the Zeebe workflow engine.
 */
@Service
@Slf4j
public class GrabberProcessingService {

    // The ZeebeClient used to interact with the Zeebe workflow engine
    private final ZeebeClient zeebeClient;

    /**
     * This is the constructor for the GrabberProcessingService.
     * It uses Spring's @Autowired annotation to automatically inject the ZeebeClient.
     * @param zeebeClient The ZeebeClient used to interact with the Zeebe workflow engine.
     */
    @Autowired
    public GrabberProcessingService(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    /**
     * This method processes the "grabGoods" task.
     * It uses Camunda's @ZeebeWorker annotation to indicate that it is a worker for the "grabGoods" task.
     * @param job The job that is being processed.
     */
    @ZeebeWorker(type = "grabGoods", name = "grabGoodsProcessor")
    public void grabGoods(final ActivatedJob job) {
        String orderDetails = job.getVariablesAsMap().get("orderDetails").toString();
        String variables = String.format("{\"orderDetails\": \"%s\"}", orderDetails);

        WorkflowLogger.info(log, "grabGoods", "Processing order: " + job.getProcessInstanceKey() + " - " + orderDetails);
        if (Math.random() < 0.5){
            WorkflowLogger.info(log, "grabGoods", "Complete order: " + job.getProcessInstanceKey() + " - " + orderDetails);
            zeebeClient.newCompleteCommand(job.getKey())
                    .variables(variables)
                    .send()
                    .join(); // Synchronous completion, remove join() for asynchronous
        } else {
            WorkflowLogger.info(log, "grabGoods", "Failed order: " + job.getProcessInstanceKey() + " - " + orderDetails);
        }
    }
}