package ch.unisg.delivery.camunda;

import ch.unisg.delivery.domain.Order;
import ch.unisg.delivery.domain.OrderRegistry;
import ch.unisg.delivery.utils.WorkflowLogger;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * This is a service class that processes delivery tasks.
 * It uses the CamundaService to interact with the Camunda engine.
 */
@Service
@Slf4j
public class DeliveryProcessingService {

    private final CamundaService camundaMessageSenderService;

    /**
     * Constructor for the DeliveryProcessingService class.
     * @param camundaMessageSenderService The CamundaService instance to be used for interactions with the Camunda engine.
     */
    @Autowired
    public DeliveryProcessingService(CamundaService camundaMessageSenderService) {
        this.camundaMessageSenderService = camundaMessageSenderService;
    }

    /**
     * This method registers an order in the delivery station.
     * @param job The job that contains the details of the order.
     */
    @ZeebeWorker(type = "registerOrder", name = "registerOrderProcessor")
    public void registerOrder(final ActivatedJob job) {
        Map<String, Object> orderVariables = (Map<String, Object>) job.getVariablesAsMap().get("order");

        // Now you can access individual properties within the 'order' object
        String orderColor = (String) orderVariables.get("orderColor");
        String orderId = (String) orderVariables.get("orderId");
        String deliveryMethod = (String) orderVariables.get("deliveryMethod");

        WorkflowLogger.info(log, "registerOrder","Processing order: " + job.getProcessInstanceKey() + " - " + orderColor);

        OrderRegistry.addOrder(new Order(orderId, orderColor, deliveryMethod));

        WorkflowLogger.info(log, "registerOrder","Complete order: " + job.getProcessInstanceKey() + " - " + orderColor);

        camundaMessageSenderService.sendCompleteCommand(job.getKey(), job.getVariables());

//        if (orderColor.contains("blue")) {
//            WorkflowLogger.info(log, "registerOrder","Failed order: " + job.getProcessInstanceKey() + " - " + orderColor);
//            camundaMessageSenderService.throwErrorCommand("DeliveryStationError",
//                    String.format("Orders with %s goods fail in delivery station", orderColor), job.getKey());
//        } else {
//            WorkflowLogger.info(log, "registerOrder","Complete order: " + job.getProcessInstanceKey() + " - " + orderColor);
//            camundaMessageSenderService.sendCompleteCommand(job.getKey(), variables);
//        }
    }
}

