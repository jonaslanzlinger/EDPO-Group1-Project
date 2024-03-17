# Demo Instructions
1. Clone the repository
2. In the project root, run `docker compose up`
   * This will start the following containers:
     * zookeeper
     * kafka
     * order
     * warehouse
     * grabber
     * delivery
3. Restart some services
   * run `docker stop edpo-group1-project-warehouse-1 edpo-group1-project-grabber-1 edpo-group1-project-delivery-1`
   * run `docker start edpo-group1-project-warehouse-1 edpo-group1-project-grabber-1 edpo-group1-project-delivery-1`
4. Open the browser and go to `http://localhost:8080/`
   * Login with: demo/demo
5. Open another browser tab and go to `http://localhost:8080/order.html`
   * issue some orders...


## Simulating the Factory
For testing purposes,
we have the simple service factorysimulator that sends the same mqtt messages as the factory would.
Currently, it sends one message every second from the log file and sends each through a station-specific topic.

To start the sending of the messages, make sure that the containers are running and then simpy set a GET request
to http://localhost:8085/send

Currently, the factorylistener is also implemented the same way.
So to ensure that all messages are communicated through all services, 
go to http://localhost:8084/send to also connect the factorylistener.
