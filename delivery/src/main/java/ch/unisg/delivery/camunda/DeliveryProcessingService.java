package ch.unisg.delivery.camunda;

import ch.unisg.delivery.domain.Order;
import ch.unisg.delivery.domain.OrderRegistry;
import ch.unisg.delivery.kafka.dto.MonitorUpdateDto;
import ch.unisg.delivery.kafka.producer.MonitorDataProducer;
import ch.unisg.delivery.utils.WorkflowLogger;

import com.google.errorprone.annotations.Var;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.Map;

import static ch.unisg.delivery.kafka.producer.MonitorDataProducer.MonitorStatus.success;

/**
 * This is a service class that processes delivery tasks.
 * It uses the CamundaService to interact with the Camunda engine.
 */
@Service
@Slf4j
public class DeliveryProcessingService {

    private final CamundaService camundaMessageSenderService;

    private final MonitorDataProducer monitorDataProducer;

    /**
     * Constructor for the DeliveryProcessingService class.
     *
     * @param camundaMessageSenderService The CamundaService instance to be used for interactions with the Camunda engine.
     * @param monitorDataProducer The MonitorDataProducer instance to be used for sending monitoring data.
     */
    @Autowired
    public DeliveryProcessingService(CamundaService camundaMessageSenderService, MonitorDataProducer monitorDataProducer) {
        this.camundaMessageSenderService = camundaMessageSenderService;
        this.monitorDataProducer = monitorDataProducer;
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
     * @param job The job that contains the details of the order.
     * @param order The order to be processed.
     */
    @JobWorker(type = "retrieveColor", name = "retrieveColorProcessor", autoComplete = false)
    public void retrieveColor(final ActivatedJob job, @Variable Order order) {

        WorkflowLogger.info(log, "retrieveColor","Retrieving color at light sensor...");

        // Wrap calling the color REST endpoint in the factory within a Hystrix Circuit breaker
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://factorysimulator:8085/vgr/read_color"))
                .GET()
                .build();

        HystrixCommand.Setter config = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("retrieveColor"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(10_000));

        HttpResponse response = new HystrixCommand<HttpResponse>(config) {

            @Override
            protected HttpResponse<String> run() throws Exception {
                HttpClient client = HttpClient.newHttpClient();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                WorkflowLogger.info(log, "retrievedColor","Color retrieved: " + response.body());

                String variables = "{ \"retrievedColor\": \"" + response.body() + "\"}";

                camundaMessageSenderService.sendCompleteCommand(job.getKey(), variables);
                return null;
            }

            @Override
            protected HttpResponse<String> getFallback() {
                WorkflowLogger.info(log, "retrieveColor","Circuit breaker - Fallback occurred.");

                camundaMessageSenderService.throwErrorCommand("FactoryTimeoutError", "Error retrieving color at light sensor.", job.getKey());
                return null;
            }
        }.execute();

        monitorDataProducer.sendMonitorUpdate(order.getOrderId(), "retrieveColor", success.name());
    }

    /**
     * This method matches the color of the order to the color retrieved at the light sensor.
     * @param job The job that contains the detected color at the light sensor.
     * @param order The order to be processed.
     */
    @JobWorker(type = "matchColorToOrder", name = "matchColorToOrderProcessor", autoComplete = false)
    public void matchColorToOrder(final ActivatedJob job, @Variable Order order,
                                  @Variable String retrievedColor) {
        Order matchedOrder = OrderRegistry.popNextOrderByColor(retrievedColor);

        String orderVariables = null;
        if (matchedOrder == null) {
            WorkflowLogger.info(log, "matchColorToOrder","No order found for color: " + retrievedColor);
            OrderRegistry.pop();
            orderVariables =
                    "{\"matchFound\": \"false\", \"order\": {\"orderColor\": \"" + order.getOrderColor() + "\"," +
                    "\"orderId\": \"" + order.getOrderId() + "\"," +
                    "\"deliveryMethod\": \"" + order.getDeliveryMethod() + "\"}}";
        } else {
            WorkflowLogger.info(log, "matchColorToOrder","Order found for color: " + retrievedColor);
            orderVariables = "{\"matchFound\": \"true\", \"order\": {\"orderColor\": \"" + order.getOrderColor() + "\"," +
                    "\"orderId\": \"" + order.getOrderId() + "\"," +
                    "\"deliveryMethod\": \"" + order.getDeliveryMethod() + "\"}}";
        }

        camundaMessageSenderService.sendCompleteCommand(job.getKey(), orderVariables);

        monitorDataProducer.sendMonitorUpdate(order.getOrderId(), "matchColorToOrder", success.name());
    }

    /**
     * This method processes the order that has been matched.
     * @param order The order that has been matched.
     */
    @JobWorker(type = "orderMatched", name = "orderMatchedProcessor")
    public void orderMatched(@Variable Order order) {
        WorkflowLogger.info(log, "orderMatched","Order matched: " + order.getOrderId() + " - " + order.getOrderColor());
        monitorDataProducer.sendMonitorUpdate(order.getOrderId(), "orderMatched", success.name());
    }
}