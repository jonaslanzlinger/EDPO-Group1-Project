package ch.unisg.grabber.camunda;

import ch.unisg.grabber.domain.GrabberStatusService;
import ch.unisg.grabber.domain.Order;
import ch.unisg.grabber.kafka.producer.MonitorDataProducer;
import ch.unisg.grabber.utils.WorkflowLogger;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static ch.unisg.grabber.kafka.producer.MonitorDataProducer.MonitorStatus.success;

/**
 * This is a service class for processing Grabber tasks.
 * It uses Spring's @Service annotation to indicate that it is a service class.
 * It uses Camunda's ZeebeClient to interact with the Zeebe workflow engine.
 */
@Service
@Slf4j
public class GrabberProcessingService {

    private final MonitorDataProducer monitorDataProducer;

    private final GrabberStatusService grabberStatusService;

    /**
     * This is the constructor for the GrabberProcessingService.
     * It uses Spring's @Autowired annotation to automatically inject the ZeebeClient.
     *
     */
    @Autowired
    public GrabberProcessingService(MonitorDataProducer monitorDataProducer, GrabberStatusService grabberStatusService) {
        // The ZeebeClient used to interact with the Zeebe workflow engine
        this.monitorDataProducer = monitorDataProducer;
        this.grabberStatusService = grabberStatusService;
    }

    /**
     * This method processes the "grabGoods" task.
     * It uses Camunda's @ZeebeWorker annotation to indicate that it is a worker for the "grabGoods" task.
     *
     * @param order The order to process.
     */
    @JobWorker(type = "grabGoods", name = "grabGoodsProcessor")
    public void grabGoods(@Variable Order order) throws URISyntaxException, InterruptedException {
        WorkflowLogger.info(log, "grabGoods",
                "Processing order: - " + order.getOrderColor());
        WorkflowLogger.info(log, "grabGoods",
                "Complete order: - " + order.getOrderColor());

        // while loop to check light sensor
        while(grabberStatusService.getLatestStatus().isI1_light_barrier()) {
            Thread.sleep(100);
        }
        pickUpProduct();

        monitorDataProducer.sendMonitorUpdate(order.getOrderId(), "grabGoods", success.name());
    }

    /**
     * This method invokes the pickUpProduct API to pick up the product from the light barrier and
     * transport it to the color detection delivery pick up station.
     *
     * @throws URISyntaxException
     */
    private void pickUpProduct() throws URISyntaxException {
        // TODO
        String url = "http://host.docker.internal:5001/vgr/pick_up_and_transport?machine=vgr_1&start=high_bay_warehouse&end=color_detection_delivery_pick_up_station";
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        }
    }
}