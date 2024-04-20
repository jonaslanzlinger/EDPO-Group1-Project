# Monitoring Service
The monitoring service is responsible for the monitoring of the processes in the smart factory.
It is implemented as a microservice and communicates with the other parts of the application through Kafka, Camunda and a simple REST API.
Monitoring messages are published by the distinct services at specific points in time and are consumed by the monitoring service.
The monitoring service then processes these messages and stores them in memory.
The user interface can then access the monitoring service to retrieve the stored data and display it to the user.

# URLs
Monitoring UI
> http://localhost:8086/index.html

REST API:  
Retrieve all messages for all orders: 
> GET http://localhost:8086/api/monitoring/orders

Retrieve all messages for a specific orderId: 
> GET http://localhost:8086/api/monitoring/orders/{orderId}

# Domain
The domain contains a single class [MonitoringStore](./src/main/java/ch/unisg/monitoring/domain/MonitoringStore.java) that is responsible for storing the monitoring data.
The class is thread safe and can be accessed and modified safely by multiple threads.
At this moment, the monitoring store is implemented as a simple in-memory store.

# Kafka
The monitoring service is subscribed to the "monitoring" topic via the [MonitorDataConsumer](./src/main/java/ch/unisg/monitoring/kafka/consumer/MonitorDataConsumer.java) and receives monitoring messages from the other services.
Monitoring data is received in the form of a [MonitorUpdateDto](./src/main/java/ch/unisg/monitoring/kafka/dto/MonitorUpdateDto.java) object.

# Camunda
The monitoring service is triggered by a camunda "external task" at the beginning of an order process via the "newOrderToMonitor" job in the [MonitoringProcessingService](./src/main/java/ch/unisg/monitoring/camunda/MonitoringProcessingService.java) class.  
The current implementation does only log a message to the console indicating that monitoring has started.
Furthermore the [CamundaService](./src/main/java/ch/unisg/monitoring/camunda/CamundaService.java) class is a utility class responsible for the communication with the camunda engine.
It is currently unused since we have migrated from @ZeebeWorker to @JobWorker and will be removed in the future.

# REST API / Emitter
The monitoring service provides a simple REST API to retrieve the monitoring data via the [MonitoringRestController](./src/main/java/ch/unisg/monitoring/rest/MonitoringRestController.java) class.
Monitoring data can be retrieved either by sending a GET request to the `/api/monitoring/orders` endpoint in order to retrieve all monitoring data or by sending a GET request to the `/api/monitoring/orders/{orderId}` endpoint in order to retrieve the monitoring data for a specific order.
Moreover the API also provides an `/api/updates` endpoint that is used by the Monitoring UI to retrieve the latest monitoring data periodically.

# Monitoring UI
The Monitoring UI can be accessed in the browser by navigating to http://localhost:8086/index.html .
It is implemented as a simple HTML page with a bit of javascript that uses the `/api/updates` endpoint to retrieve the latest monitoring data.
The monitoring data is then displayed in a table on the page and shows all Events/Commands that have been consumed and stored by the monitoring service.

# Configuration
The monitoring service is configured via the [application.properties](./src/main/resources/application.yml) file.
It runs on `localhost:8086` and connects to the Kafka broker on `localhost:9092`.