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
        String orderColor = job.getVariablesAsMap().get("orderColor").toString();


        WorkflowLogger.info(log, "checkGoods","Processing order: " + job.getProcessInstanceKey() + " - " + orderColor);

        // TODO: Remove hardcoded stuff here
        if (orderColor.contains("red")) {
            WorkflowLogger.info(log, "checkGoods", "Failed Order: " + job.getProcessInstanceKey()+ " - " + orderColor);
            camundaMessageSenderService.throwErrorCommand("GoodsNotAvailable",
                    String.format("No %s goods available", orderColor), job.getKey());

        } else {
            WorkflowLogger.info(log, "checkGoods", "Complete order: " + job.getProcessInstanceKey()+ " - " + orderColor);
            camundaMessageSenderService.sendCompleteCommand(job.getKey(), job.getVariables());
        }
    }
}