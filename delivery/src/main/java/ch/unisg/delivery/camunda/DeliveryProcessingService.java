package ch.unisg.delivery.camunda;

import ch.unisg.delivery.domain.Order;
import ch.unisg.delivery.domain.OrderRegistry;
import ch.unisg.delivery.utils.WorkflowLogger;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        String orderDetails = job.getVariablesAsMap().get("orderDetails").toString();
        String variables = String.format("{\"orderDetails\": \"%s\"}", orderDetails);

        WorkflowLogger.info(log, "registerOrder","Processing order: " + job.getProcessInstanceKey() + " - " + orderDetails);

        OrderRegistry.addOrder(new Order(orderDetails));

        WorkflowLogger.info(log, "registerOrder","Complete order: " + job.getProcessInstanceKey() + " - " + orderDetails);
        camundaMessageSenderService.sendCompleteCommand(job.getKey(), variables);

//        if (orderDetails.contains("blue")) {
//            WorkflowLogger.info(log, "registerOrder","Failed order: " + job.getProcessInstanceKey() + " - " + orderDetails);
//            camundaMessageSenderService.throwErrorCommand("DeliveryStationError",
//                    String.format("Orders with %s goods fail in delivery station", orderDetails), job.getKey());
//        } else {
//            WorkflowLogger.info(log, "registerOrder","Complete order: " + job.getProcessInstanceKey() + " - " + orderDetails);
//            camundaMessageSenderService.sendCompleteCommand(job.getKey(), variables);
//        }
    }
}

