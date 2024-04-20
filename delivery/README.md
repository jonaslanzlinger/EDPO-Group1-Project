# Delivery Service
The delivery service is responsible for the part of the business process that interacts with the "delivery 
& pickup" station of the smart factory. It is implemented as a microservice and communicates with the 
other parts of the application through Kafka, Camunda and simple REST API.

## Domain
In the domain we have the necessary classes and services, that enable us to have a digital representation 
of the delivery & pickup station. The [VGR_1](src%2Fmain%2Fjava%2Fch%2Funisg%2Fdelivery%2Fdomain%2FVGR_1.java) 
class is the Data class that maps to the data received from the smart factory.
The [DeliveryStatusService](src%2Fmain%2Fjava%2Fch%2Funisg%2Fdelivery%2Fdomain%2FDeliveryStatusService.java)
manages the current status of the delivery & pickup station.
The instance is atomic and can be accessed and modified safely by multiple threads. If the status gets 
updated, the service will check, if the delivery & pickup station is holding a waiting workpiece in the 
pickup slot. If this is the case, the service will emit a new Camunda message, called 
"ProductAtLightSensor". Because we don't know the exact namings of the station, we just take all signals 
from the two light sensors as a trigger for the next step in the process. (this is going to be changed in 
upcoming iterations)

Next, we have the [OrderRegistry](src%2Fmain%2Fjava%2Fch%2Funisg%2Fdelivery%2Fdomain%2FOrderRegistry.java) 
that manages the FIFO queue of orders that are waiting to be processed.

The [DeliveryProcessingService](src%2Fmain%2Fjava%2Fch%2Funisg%2Fdelivery%2Fcamunda%2FDeliveryProcessingService.java) is the main service class of the delivery service. It is responsible for updating the Delivery & 
Pickup station (i.e. the OrderRegistry).

## Kafka
The Smart Factory continuously produces events with information about the current state of the factory. 
These updates are received at the FactoryListener and then processed and published in the correct Kafka 
topic (VGR_1).
The Delivery service is subscribed to this topic in the [MessageConsumer](src%2Fmain%2Fjava%2Fch%2Funisg%2Fdelivery%2Fkafka%2Fconsumer%2FMessageConsumer.java)
and can so always update its current representation of the Delivery & Pickup station.

The Delivery service also takes part in the choreography part of the process. It communicates with the 
monitoring service through the [MonitorDataProducer](src%2Fmain%2Fjava%2Fch%2Funisg%2Fdelivery%2Fkafka%2Fproducer%2FMonitorDataProducer.java), which sends updates of the current process to the monitoring service at specific points in time.

## Camunda
The Delivery service is also involved in the orchestration part of the process. The service is registered as a Zeebe Client with our workflow engine and can receive tasks from the engine.
The Delivery service then executes these tasks and sends the results back to the engine. The Delivery 
service is also responsible for the error handling of the process. If an error occurs, the Delivery 
service will catch the error and send a message to the engine to handle the error (e.g. if the 
FactorySimulator service is not responding or if the color at the light sensor can not be matched to the 
next order in the FIFO queue). 
The Wrapper Class for the Zeebe Client, that is used to communicate with the engine, can be found 
[here](src%2Fmain%2Fjava%2Fch%2Funisg%2Fdelivery%2Fcamunda%2FCamundaService.java).
All worker methods are implemented in the [DeliveryProcessingService](src%2Fmain%2Fjava%2Fch%2Funisg%2Fdelivery%2Fcamunda%2FDeliveryProcessingService.java) Class.
The methods utilize the Zeebe Annotations to reduce boilerplate code and increase readability as well as 
resilience.
```java
@JobWorker(type = "registerOrder", name = "registerOrderProcessor")
public void registerOrder(@Variable Order order) {
    WorkflowLogger.info(log, "registerOrder","Processing order: - " + order.getOrderColor());
    OrderRegistry.addOrder(order);

    monitorDataProducer.sendMonitorUpdate(order.getOrderId(), "registerOrder", success.name());
}
```
Let's have a look at the behavior of the registerOrder method.
The JobWorker Annotation is used to define the type of task that the method is supposed to handle. The method then the variables that are passed to the task. 
The method then processes the task and sends the result back to the engine because the flag "autoComplete" 
is default set to "true" 
Additionally Zeebe allows us to define @Variable annotations to automatically map the variables that are 
passed to the task to the method parameters. This also reduces boilerplate and increases readability.

![bpmnDelivery.png](..%2Fdocs%2Fimages%2Fbmpns%2FbpmnDelivery.png)

## REST API
The Delivery service also provides a REST API to interact with the service. The API is used to enable 
easier testing, debugging, and monitoring of the service. The API is implemented in the
[DeliveryServiceRestController](src%2Fmain%2Fjava%2Fch%2Funisg%2Fdelivery%2Frest%2FDeliveryServiceRestController.java) class.
Here, you can find a list of all available endpoints:
* GET /status - Get the current status of the delivery & pickup station
* GET /orders - Get all orders that are currently in the FIFO queue
* GET /triggerLightSensor - Trigger the light sensor to simulate a new workpiece at the station