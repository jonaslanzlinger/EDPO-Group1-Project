# Demo Instructions
1. Clone the repository
2. In the project root, run `docker compose up --build`
   * This will start the following containers:
     * zookeeper
     * kafka
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
6. You can now inspect the Order process at `http://localhost:8080/`
7. You can now inspect the Warehouse process at `http://localhost:8081/`

Note: Perform the user tasks where needed to continue the processes.
