# Order Service

> http://localhost:8080/orders.html

The order service is the centerpiece of our business process. The service is responsible for managing the
orders that are received from the customer and for orchestrating the process of the smart factory. The
service is implemented as a microservice and communicates with the other parts of the application through
Kafka, Camunda and simple REST API. The communication with the User Interface is realized through an SSE 
(Server-side Emitter).

In the following, we will describe the rough flow of the whole process:
* The customer places an order through the User Interface
* The order service receives the order and registers it
* The order service sends a kafka message to the monitoring service to inform it about the new order, and 
  that the order should get processed
* As the first station, the order gets sent to the warehouse station
* If the warehouse station is not producing an error, the order gets sent to the grabber station
* If the grabber station is not producing an error, the order gets sent to the delivery station
* If the delivery station is not producing an error, the order gets delivered to the customer, either via 
  pickup or the postal way.
* If anywhere in the process an error occurs, the order service will catch the error and create a user 
  task, set the corresponding status of the order and terminate the camunda workflow process for this order.
* At each major step in the process, the order service will send a message to the monitoring service to 
  inform it about the current status of the order.

![bpmnOrder.png](..%2Fdocs%2Fimages%2Fbmpns%2FbpmnOrder.png)

## Domain
In the domain we have the necessary classes to represent Orders, the OrderRegistry and a replica of the 
current stock in the warehouse.

## Kafka
The order service is consuming the topic "warehouse" to receive updates about the current stock in the
warehouse. This implementation can be found in the [MessageConsumer](src%2Fmain%2Fjava%2Fch%2Funisg%2Forder%2Fkafka%2Fconsumer%2FMessageConsumer.java) class.
The order service is also producing messages to the topic "monitoring" to inform the monitoring service
about the current status of the orders. This implementation can be found in the [MonitorDataProducer](src%2Fmain%2Fjava%2Fch%2Funisg%2Forder%2Fkafka%2Fproducer%2FMonitorDataProducer.java) class.

## Camunda
The Delivery service is mainly involved in the orchestration part of the process and takes a very 
important role in it. The service is registered as a Zeebe Client with our workflow engine and can receive 
tasks from the engine.
The Order service then executes these tasks and sends the results back to the engine. The Order 
service is also responsible for the error handling of the process. If any given error is occuring in the 
whole process, it will be catched by the camunda Order workflow and a user task will be created accordingly.
The Wrapper Class for the Zeebe Client, that is used to communicate with the engine, can be found in the 
[ProcessStarterService](src%2Fmain%2Fjava%2Fch%2Funisg%2Forder%2Fservices%2FProcessStarterService.java) class.
All worker methods are implemented in this class.
The methods utilize the Zeebe Annotations to reduce boilerplate code and increase readability as well as 
resilience.
```java
@JobWorker(type = "setProgressDelivered", name = "setProgressDeliveredProcessor")
public void setProgressDelivered(@Variable Order order) {
    sleep(2000);
    Objects.requireNonNull(OrderRegistry.getOrderById(order.getOrderId())).setProgress("delivered");

    monitorDataProducer.sendMonitorUpdate(order.getOrderId(), "setProgressDelivered", success.name());
}
```
Let's have a look at the behavior of the setProgressDelivered method.
The JobWorker Annotation is used to define the type of task that the method is supposed to handle. The method then the variables that are passed to the task. 
The method then processes the task and sends the result back to the engine because the flag "autoComplete" 
is default set to "true".
Additionally Zeebe allows us to define @Variable annotations to automatically map the variables that are 
passed to the task to the method parameters. This also reduces boilerplate and increases readability.

Furthermore, the Order service is also providing a [StockService](src%2Fmain%2Fjava%2Fch%2Funisg%2Forder%2Fservices%2FStockService.java) that
is taking care of the replica of the current stock in the warehouse (event-carried state transfer pattern).

## REST API
The Order service provides the following REST API endpoint:
* PUT /api/order/{color}/{deliveryMethod} - Place a new order with the given color and delivery method
* GET /api/updates - Get the current status of all orders (SSE)
* GET /api/currentStock - Get the current stock in the warehouse (SSE)

## User Interface
The UI can be accessed in the browser by navigating to http://localhost:8080/orders.html.

It is implemented as a simple HTML page with a bit of javascript that uses the `/api/updates` endpoint to 
retrieve the latest order data, and the `/api/currentStock` endpoint to retrieve the current stock in the
warehouse. By using Server-Sent Events (SSE) the UI is able to receive updates from the server in 
real-time without the need to poll the server periodically (server-push).
The UI displays the current status of all orders on a visual representation of the smart factory. The
current stock in the warehouse is displayed by labeling the products with "out of stock" or "order".