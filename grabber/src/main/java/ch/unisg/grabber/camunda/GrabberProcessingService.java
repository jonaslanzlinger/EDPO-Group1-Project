package ch.unisg.grabber.camunda;

import ch.unisg.grabber.domain.Order;
import ch.unisg.grabber.kafka.producer.MonitorDataProducer;
import ch.unisg.grabber.utils.WorkflowLogger;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static ch.unisg.grabber.kafka.producer.MonitorDataProducer.MonitorStatus.success;
import static ch.unisg.grabber.utils.Utility.sleep;

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

    private final MonitorDataProducer monitorDataProducer;

    /**
     * This is the constructor for the GrabberProcessingService.
     * It uses Spring's @Autowired annotation to automatically inject the ZeebeClient.
     *
     * @param zeebeClient         The ZeebeClient used to interact with the Zeebe workflow engine.
     * @param monitorDataProducer
     */
    @Autowired
    public GrabberProcessingService(ZeebeClient zeebeClient, MonitorDataProducer monitorDataProducer) {
        this.zeebeClient = zeebeClient;
        this.monitorDataProducer = monitorDataProducer;
    }

    /**
     * This method processes the "grabGoods" task.
     * It uses Camunda's @ZeebeWorker annotation to indicate that it is a worker for the "grabGoods" task.
     *
     * @param order The order to process.
     */
    @JobWorker(type = "grabGoods", name = "grabGoodsProcessor")
    public void grabGoods(@Variable Order order) {
        WorkflowLogger.info(log, "grabGoods",
                "Processing order: - " + order.getOrderColor());
        sleep(5000); // Simulate processing time
        WorkflowLogger.info(log, "grabGoods",
                "Complete order: - " + order.getOrderColor());

        monitorDataProducer.sendMonitorUpdate(order.getOrderId(), "grabGoods", success.name());
    }
}