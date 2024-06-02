![banner.png](docs%2Fimages%2Fbanner.png)

# EDPO - Group 1
Our Project for the Event-Driven and Process-Oriented Architecture course at the University of St. Gallen.
We are building an Application, that interacts with the Smart Factory and simulates a process of ordering a product. 
A user can order a product, which will be removed from the HighBay Warehouse, transported to the Grabber, then to the Delivery Station and finally delivered to the user.

## Documents
* Service READMEs: In the respective service folders
* Architectual Decision Records (ADRs): [adrs](docs%2Fadrs)
* BMPN Diagrams: [bmpns](docs%2Fimages%2Fbmpns)
* Sequence Diagram: [sequence_diagram.pdf](docs%2Fsequence_diagram%2Fsequence_diagram.pdf)
* Presentation PDF: [intermediate_presentation.pdf](docs%2Fintermediate_presentation.pdf)
* Word Document, hand-in: [intermediate_report.pdf](docs%2Fintermediate_report.pdf)
* Demo Instructions: Below
* Previous hand-ins, Exercises 1-4: [https://github.com/KaiTries/EDPO-Group1](https://github.com/KaiTries/EDPO-Group1)

## Services
The application is built with a microservice architecture and uses Kafka, Camunda and MQTT to communicate between the services.
The services are (click on the links for their respective READMEs):
* [Order Service](order/README.md)
* [Warehouse Service](warehouse/README.md)
* [Grabber Service](grabber/README.md)
* [Delivery Service](delivery/README.md)
* [Factory Simulator](factorysimulator/README.md)
* [Factory Listener](factorylistener/README.md)
* [Monitoring Service](monitoring/README.md)
* [Streamprocessor Service](streamprocessor/)

## Demo Instructions
1. Clone the repository
2. In the project root, run `docker compose up --build`
   * This will start the following containers:
     * zookeeper
     * kafka
     * mqtt
     * order
     * warehouse
     * grabber
     * delivery
     * factorysimulator
     * factorylistener
     * monitoring
3. Open the browser and go to `http://localhost:8085/send` to start the factory simulator.
MQTT messages will be produced each second from now.
4. Open another browser tab and go to `http://localhost:8084/send` to start the factory listener.
The MQTT messages from the factory will now get relayed to the other services via Kafka.
5. First, we need to fill up the stock of the warehouse. Issue a GET request to 
   `http://localhost:8081/setStatus`.
6. Open another browser tab and go to `http://localhost:8080/order.html`
   * issue some orders...
7. The order should be processed automatically through the warehouse and the grabber.
8. Once the order arrives in the delivery station, issue a GET request to 
   `http://localhost:8083/triggerLightSensor` to simulate the arrival of a workpiece at the light sensor 
   in the delivery station. (Note: The retrieved color will now always be white, therefore, orders with 
   the color blue or red, will always result in a failed delivery.)
9. Depending on whether the delivery was successful or not, you will need to complete a user task within 
   the camunda tasklist.
10. Hint: Check the logs of the services to see the messages being processed.
11. Hint: Check online through camunda the process instances and tasks.
12. Hint: Check the monitoring service to see the metrics of the service. (`http://localhost:8086/`)
