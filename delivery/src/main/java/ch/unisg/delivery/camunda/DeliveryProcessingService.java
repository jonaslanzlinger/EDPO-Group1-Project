package ch.unisg.delivery.camunda;

import ch.unisg.delivery.domain.Order;
import ch.unisg.delivery.domain.OrderRegistry;
import ch.unisg.delivery.utils.WorkflowLogger;
import com.google.api.Http;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLOutput;
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
    }

    /**
     * This method retrieves the color of the order at the light sensor.
     * @param job The job that contains the details of the order.
     */
    @ZeebeWorker(type = "retrieveColor", name = "retrieveColorProcessor")
    public void retrieveColor(final ActivatedJob job) {

        WorkflowLogger.info(log, "retrieveColor","Retrieving color at light sensor...");

        // Wrap calling the color REST endpoint in the factory within a Hystrix Circuit breaker
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://factorysimulator:8085/vgr/read_color"))
                .GET()
                .build();

        HystrixCommand.Setter config = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("retrieveColor"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(2_000));

        HttpResponse response = new HystrixCommand<HttpResponse>(config) {

            @Override
            protected HttpResponse<String> run() throws Exception {
                HttpClient client = HttpClient.newHttpClient();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                WorkflowLogger.info(log, "retrieveColor","Color retrieved: " + response.body());

                String variables = "{ \"orderColor\": \"" + response.body() + "\"}";

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
    }

    /**
     * This method matches the color of the order to the color retrieved at the light sensor.
     * @param job The job that contains the detected color at the light sensor.
     */
    @ZeebeWorker(type = "matchColorToOrder", name = "matchColorToOrderProcessor")
    public void matchColorToOrder(final ActivatedJob job) {

        String orderColor = (String) job.getVariablesAsMap().get("orderColor");

        Order order = OrderRegistry.popNextOrderByColor(orderColor);

        String orderVariables = null;
        if (order == null) {
            WorkflowLogger.info(log, "matchColorToOrder","No order found for color: " + orderColor);

            orderVariables = "{\"matchFound\": \"false\", \"order\": {\"orderColor\": \"" + order.getOrderColor() + "\"," +
                    "\"orderId\": \"" + order.getOrderId() + "\"," +
                    "\"deliveryMethod\": \"" + order.getDeliveryMethod() + "\"}}";
        } else {
            WorkflowLogger.info(log, "matchColorToOrder","Order found for color: " + orderColor);

            orderVariables = "{\"matchFound\": \"true\", \"order\": {\"orderColor\": \"" + order.getOrderColor() + "\"," +
                    "\"orderId\": \"" + order.getOrderId() + "\"," +
                    "\"deliveryMethod\": \"" + order.getDeliveryMethod() + "\"}}";
        }

        camundaMessageSenderService.sendCompleteCommand(job.getKey(), orderVariables);
    }
}