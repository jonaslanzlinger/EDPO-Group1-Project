package ch.unisg.monitoring.camunda;

import ch.unisg.monitoring.utils.WorkflowLogger;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * @param orderId The ID of the order to monitor.
     */
    @JobWorker(type = "newOrderToMonitor", name = "newOrderToMonitorProcessor")
    public void newOrderToMonitor(@Variable String orderId) {
        WorkflowLogger.info(log, "newOrderToMonitor", "Starting Monitoring for order: - " + orderId);
    }
}