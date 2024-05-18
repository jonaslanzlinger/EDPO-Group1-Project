package ch.unisg.delivery.camunda;

import ch.unisg.delivery.domain.DeliveryStatusService;
import ch.unisg.delivery.domain.Order;
import ch.unisg.delivery.domain.OrderRegistry;
import ch.unisg.delivery.kafka.producer.MonitorDataProducer;
import ch.unisg.delivery.utils.WorkflowLogger;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import static ch.unisg.delivery.kafka.producer.MonitorDataProducer.MonitorStatus.failed;
import static ch.unisg.delivery.kafka.producer.MonitorDataProducer.MonitorStatus.success;

/**
 * This is a service class that processes delivery tasks.
 * It uses the CamundaService to interact with the Camunda engine.
 */
@Service
@Slf4j
public class DeliveryProcessingService {

    private final CamundaService camundaMessageSenderService;
    private final DeliveryStatusService deliveryStatusService;
    private final MonitorDataProducer monitorDataProducer;

    /**
     * Constructor for the DeliveryProcessingService class.
     *
     * @param camundaMessageSenderService The CamundaService instance to be used for interactions with the Camunda engine.
     * @param monitorDataProducer The MonitorDataProducer instance to be used for sending monitoring data.
     */
    @Autowired
    public DeliveryProcessingService(CamundaService camundaMessageSenderService, MonitorDataProducer monitorDataProducer, DeliveryStatusService deliveryStatusService) {
        this.camundaMessageSenderService = camundaMessageSenderService;
        this.monitorDataProducer = monitorDataProducer;
        this.deliveryStatusService = deliveryStatusService;
    }

    /**
     * This method registers an order in the delivery station.
     * @param order The order to be registered.
     */
    @JobWorker(type = "registerOrder", name = "registerOrderProcessor")
    public void registerOrder(@Variable Order order) {
        WorkflowLogger.info(log, "registerOrder","Processing order: - " + order.getOrderColor());
        OrderRegistry.addOrder(order);

        monitorDataProducer.sendMonitorUpdate(order.getOrderId(), "registerOrder", success.name());
    }

    /**
     * This method retrieves the color of the order at the light sensor.
     * There is a circuit breaker implemented to handle timeouts.
     *
     * @param job The job that contains the details of the order.
     * @param order The order to be processed.
     */
    @JobWorker(type = "retrieveColor", name = "retrieveColorProcessor", autoComplete = false)
    public void retrieveColor(final ActivatedJob job, @Variable Order order) {

        WorkflowLogger.info(log, "retrieveColor","Retrieving color at light sensor...");


        String variables = "{ \"retrievedColor\": \"" + deliveryStatusService.getLatestStatus().getColor() + "\"}";


        camundaMessageSenderService.sendCompleteCommand(job.getKey(),variables);
        monitorDataProducer.sendMonitorUpdate(order.getOrderId(), "retrieveColor", success.name());
    }

    /**
     * This method matches the color of the order to the color retrieved at the light sensor.
     * @param job The job that contains the detected color at the light sensor.
     * @param order The order to be processed.
     * @param retrievedColor The color detected at the light sensor.
     */
    @JobWorker(type = "checkColor", name = "checkColorProcessor", autoComplete = false)
    public void checkColor(final ActivatedJob job, @Variable Order order,
                                  @Variable String retrievedColor) {

        if (retrievedColor.equals(order.getOrderColor())) {
            WorkflowLogger.info(log, "checkColor",
                    "Color is correct: " + order.getOrderId() + " - orderColor=" + order.getOrderColor() + " detectedColor=" + retrievedColor);

            camundaMessageSenderService.sendCompleteCommand(job.getKey());
            monitorDataProducer.sendMonitorUpdate(order.getOrderId(), "checkColor", success.name());
        } else {
            WorkflowLogger.info(log, "checkColor",
                    "Color is incorrect: " + order.getOrderId() + " - orderColor=" + order.getOrderColor() + " detectedColor=" + retrievedColor);


            camundaMessageSenderService.throwErrorCommand("ColorMismatchError",
                    "Color mismatch between order and detected color at light sensor.", job.getKey());
            monitorDataProducer.sendMonitorUpdate(order.getOrderId(), "checkColor", failed.name());

        }
    }

    /**
     * This method processes the order that has been matched.
     * @param order The order that has been matched.
     */
    @JobWorker(type = "orderMatched", name = "orderMatchedProcessor")
    public void orderMatched(@Variable Order order) throws URISyntaxException, IOException, InterruptedException {
        WorkflowLogger.info(log, "orderMatched","Order matched: " + order.getOrderId() + " - " + order.getOrderColor());


        String url = "http://host.docker.internal:5001/vgr/pick_up_and_transport?machine=vgr_1&start=color_detection_delivery_pick_up_station&end=high_bay_warehouse_delivery_station";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());


        monitorDataProducer.sendMonitorUpdate(order.getOrderId(), "orderMatched", success.name());
    }
}