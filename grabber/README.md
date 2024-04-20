# Grabber Service
The grabber service is responsible for the part of the business process that interacts with the "Gripper" 
station of the smart factory. It is implemented as a microservice and communicates with the 
other parts of the application through Kafka and Camunda. This service is the simplest one of our services,
and mostly just forwards the received data to the next service in the process.

## Domain
In the domain we have the necessary classes that enable us to have a digital representation of the
"Gripper" station. The [VGR_1](src%2Fmain%2Fjava%2Fch%2Funisg%2Fgrabber%2Fdomain%2FVGR_1.java) class is the Data class that maps to the data received from the smart factory.
Since we don't have any business logic in this service, we just added this boilerplate code to have an easy 
way to start implementing the service if needed.

## Kafka
The Smart Factory continuously produces events with information about the current state of the factory. 
These updates are received at the FactoryListener and then processed and published in the correct Kafka 
topic (VGR_1).
The Gripper service is subscribed to this topic in the [MessageConsumer](src%2Fmain%2Fjava%2Fch%2Funisg%2Fgrabber%2Fkafka%2Fconsumer%2FMessageConsumer.java) class
and could update its current representation of the Gripper station.
Note: We only receive the data from the factory and don't store the current representation of the Gripper.

The Gripper service also takes part in the choreography part of the process. It communicates with the 
monitoring service through the [MonitorDataProducer](src%2Fmain%2Fjava%2Fch%2Funisg%2Fgrabber%2Fkafka%2Fproducer%2FMonitorDataProducer.java) class, which sends 
updates of the current process to the monitoring service at one point in the process.

## Camunda
The Gripper service is also involved in the orchestration part of the process. The service is registered as a 
Zeebe Client with our workflow engine and can receive tasks from the engine (up until now, only 1 task).
The Gripper service then executes these tasks and sends the results back to the engine.
All worker methods are implemented in the [GrabberProcessingService](src%2Fmain%2Fjava%2Fch%2Funisg%2Fgrabber%2Fcamunda%2FGrabberProcessingService.java) Class.
The methods utilize the Zeebe Annotations to reduce boilerplate code and increase readability as well as 
resilience.
```java
@JobWorker(type = "grabGoods", name = "grabGoodsProcessor")
public void grabGoods(@Variable Order order) {
    WorkflowLogger.info(log, "grabGoods",
    "Processing order: - " + order.getOrderColor());
    sleep(5000); // Simulate processing time
    WorkflowLogger.info(log, "grabGoods",
    "Complete order: - " + order.getOrderColor());

    monitorDataProducer.sendMonitorUpdate(order.getOrderId(), "grabGoods", success.name());
}
```
Let's have a look at the behavior of the grabGoods method.
The JobWorker Annotation is used to define the type of task that the method is supposed to handle. The method then the variables that are passed to the task. 
The method then processes the task and sends the result back to the engine because the flag "autoComplete" 
is default set to "true" 
Additionally Zeebe allows us to define @Variable annotations to automatically map the variables that are 
passed to the task to the method parameters. This also reduces boilerplate and increases readability.
For now, this method only prints out a log message and simulates processing time.

![bpmnGrabber.png](..%2Fdocs%2Fimages%2Fbmpns%2FbpmnGrabber.png)