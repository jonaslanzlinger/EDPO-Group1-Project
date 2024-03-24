package ch.unisg.warehouse.camunda;

import ch.unisg.warehouse.utils.WorkflowLogger;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is a service class that processes warehouse tasks.
 * It uses the CamundaService to interact with the Camunda engine.
 */
@Service
@Slf4j
public class WarehouseProcessingService {

    // The CamundaService instance used to send commands to the Camunda engine
    private final CamundaService camundaMessageSenderService;

    /**
     * Constructor for the WarehouseProcessingService class.
     * @param camundaMessageSenderService The CamundaService instance to be used for interactions with the Camunda engine.
     */
    @Autowired
    public WarehouseProcessingService(CamundaService camundaMessageSenderService) {
        this.camundaMessageSenderService = camundaMessageSenderService;
    }

    /**
     * This method checks the goods in the warehouse.
     * If the goods are not available, it throws an error command to the Camunda engine.
     * If the goods are available, it sends a complete command to the Camunda engine.
     * @param job The job that contains the details of the order.
     */
    @ZeebeWorker(type = "checkGoods", name = "checkGoodsProcessor")
    public void checkGoods(final ActivatedJob job) {
        String orderDetails = job.getVariablesAsMap().get("orderDetails").toString();
        String variables = String.format("{\"orderDetails\": \"%s\"}", orderDetails);

        WorkflowLogger.info(log, "checkGoods", "Processing order: " + orderDetails);

        // TODO: Remove hardcoded stuff here
        if (orderDetails.contains("red")) {
            WorkflowLogger.info(log, "checkGoods",
                    "New Throw Error Command: GoodsNotAvailable for process instance "
                            + job.getProcessInstanceKey() + " with key " + job.getKey()
                            + " orderDetails: " + orderDetails);
            camundaMessageSenderService.throwErrorCommand("GoodsNotAvailable",
                    String.format("No %s goods available", orderDetails), job.getKey());

        } else {
            WorkflowLogger.info(log, "checkGoods", "New Complete Command: process instance "
                    + job.getProcessInstanceKey() + " with key " + job.getKey() + " orderDetails: " + orderDetails);
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), variables);
        }
    }
}