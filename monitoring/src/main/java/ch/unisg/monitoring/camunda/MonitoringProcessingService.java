package ch.unisg.monitoring.camunda;

import ch.unisg.monitoring.utils.WorkflowLogger;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * This is a service class that processes warehouse tasks.
 * It uses the CamundaService to interact with the Camunda engine.
 */
@Service
@Slf4j
public class MonitoringProcessingService {

    // The CamundaService instance used to send commands to the Camunda engine
    private final CamundaService camundaMessageSenderService;


    /**
     * Constructor for the MonitoringProcessingService class.
     *
     * @param camundaMessageSenderService The CamundaService instance to be used for interactions with the Camunda engine.
     */
    @Autowired
    public MonitoringProcessingService(CamundaService camundaMessageSenderService) {
        this.camundaMessageSenderService = camundaMessageSenderService;
    }

    /**
     * This method processes a new order to monitor.
     *
     * @param job The job that contains the details of the order.
     */
    @ZeebeWorker(type = "newOrderToMonitor", name = "newOrderToMonitorProcessor")
    public void newOrderToMonitor(final ActivatedJob job) {
        // Now you can access individual properties within the 'order' object
        String orderId = (String) job.getVariable("orderId");
        WorkflowLogger.info(log, "newOrderToMonitor", "Starting Monitoring for order: " + job.getProcessInstanceKey() + " - " + orderId);
        String orderIdJson = "{\"orderId\": \"" + orderId + "\"}";
        camundaMessageSenderService.sendCompleteCommand(job.getKey(), orderIdJson);
    }
}