# Demo Instructions
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
3. Open the browser and go to `http://localhost:8085/send` to start the factory simulator.
MQTT messages will be produced each second from now.
4. Open another browser tab and go to `http://localhost:8084/send` to start the factory listener.
The MQTT messages from the factory will now get relayed to the other services via Kafka.
5. Open another browser tab and go to `http://localhost:8080/order.html`
   * issue some orders...
6. Check the logs of the services to see the messages being processed.
7. Check online through camunda the process instances and tasks.

Note: Perform the user tasks where needed to continue the processes.


# TODO
- [ ] Make Order UI responsive to Stock changes
- [ ] Correctly display error if stock is not available
- [ ] Add Hystrix Breaker to warehouse service 
