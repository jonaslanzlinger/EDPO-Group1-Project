# Warehouse Service
The warehouse service is responsible for the part of the business process that interacts with the highbay warehosue of the smart factory.
It is implemented as a microservice and communicates with the other parts of the application through Kafka, Camunda and simple REST APIs.

## Domain
In the domain we have the necessary classes and services, that enable us to have a digital representation of the HBW. 
The [HBW](warehouse/src/main/java/ch/unisg/warehouse/domain/HBW.java) class is the Data class that maps to the data received from the Smart Factory.
The [WarehouseStatusService](warehouse/src/main/java/ch/unisg/warehouse/domain/WarehouseStatusService.java) manages a single instance of the HBW. 
This instance is atomic and can be accessed and modified safely by multiple threads. It also tracks if the HBW is currently in use by a process or not. Since only one process can use the HBW at a time, this is important to prevent
concurrency issues. Processes that want to use the HBW will be put in to a thread safe queue and will be executed in order.

The [WarehouseService](warehouse/src/main/java/ch/unisg/warehouse/domain/WarehouseService.java) is the main service class of the warehouse service. 
It is responsible for updating the HBW, sending updates to the Order Service.


## Kafka
The Smart Factory continously produces events with information about the current state of the factory. 
These updates are received at the FactoryListener and then processed and published in the correct Kafka topic.
The WarehouseService is subscribed to this topic ([KafkaConsumer](warehouse/src/main/java/ch/unisg/warehouse/kafka/consumer/MessageConsumer.java)
) and can so always update its current representation of the HBW.
Once it has updated its internal state, it will propagate the stock information via kafka to the Order Service([Event-carried State Transfer](warehouse/src/main/java/ch/unisg/warehouse/domain/WarehouseService.java#L36)).

The WarehouseService also takes part in the choreography part of the process. It communicates with the monitoring service through the [MonitorDataProducer](warehouse/src/main/java/ch/unisg/warehouse/kafka/producer/MonitorDataProducer.java), which sends updates of the current process to the monitoring service at specific points in time.

## Camunda
The WarehouseService is instrumental to our business process and is therefore also involved in the orchestration part of the process. The warehouseService is registered as a Zeebe Client with our workflow engine and can receive tasks from the engine. 
The warehouseService then executes these tasks and sends the results back to the engine. The warehouseService is also responsible for the error handling of the process. If an error occurs, the warehouseService will catch the error and send a message to the engine to handle the error.
The Wrapper Class for the Zeebe Client, that is used to communicate with the engine, can be found [here](warehouse/src/main/java/ch/unisg/warehouse/camunda/CamundaService.java). All worker methods are implemented in the [WarehouseService](warehouse/src/main/java/ch/unisg/warehouse/camunda/WarehouseProcessingService.java) Class.
The methods utilize the Zeebe Annotations to reduce boilerplate and increase readability as well as resilience.
```java
@JobWorker(type = "checkGoods", name = "checkGoodsProcessor",  autoComplete = false)
public void checkGoods(final ActivatedJob job, @Variable Order order) {
    String orderColor = order.getOrderColor();
```
The JobWorker Annotation is used to define the type of task that the method is supposed to handle. The method then receives the job and the variables that are passed to the task. 
The method then processes the task and sends the result back to the engine. The autoComplete flag is set to false, since we need to manually handle task completion and specify custom error handling.
Additionally Zeebe allows us to define @Variable annotations to automatically map the variables that are passed to the task to the method parameters. This reduces boilerplate and increases readability.

![bpmnWarehouse.png](..%2Fdocs%2Fimages%2FbpmnWarehouse.png)

## REST API
The rest part of this Service is currently under construction. For now the Rest Controller allows the user to manually set the stock of the warehouse and to retrieve the current stock of the warehouse. This is useful for testing and development. 
In the latter part of this course, we will expand the REST communication to allow the warehouse Service to communicate with the Smart Factory and actually interact and control the warehouse.
