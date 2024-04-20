# EDPO - Group 1
Our Project for the Event-Driven and Process-Oriented Architecture course at the University of St. Gallen.
We are building an Application, that interacts with the Smart Factory and simulates a process of ordering a product. 
A user can order a product, which will be removed from the HighBay Warehouse, transported to the Grabber, then to the Delivery Station and finally delivered to the user.

## Services
The application is built with a microservice architecture and uses Kafka, Camunda and MQTT to communicate between the services.
The services are:
* [Order Service](order/README.md)
* [Warehouse Service](warehouse/README.md)
* [Grabber Service](grabber/README.md)
* [Delivery Service](delivery/README.md)
* [Factory Simulator](factorysimulator/README.md)
* [Factory Listener](factorylistener/README.md)
* [Monitoring Service](monitoring/README.md)

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
5. Open another browser tab and go to `http://localhost:8080/order.html`
   * issue some orders...
6. Check the logs of the services to see the messages being processed.
7. Check online through camunda the process instances and tasks.

Note: Perform the user tasks where needed to continue the processes.
