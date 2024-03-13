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