# 8. Stream Processing

Updated: 2024-05-01

## Status

Accepted

## Context
For our system, we rely heavily on event-based Kafka communication between the individual services.
Because we consume raw data from the factory, we need to process this data in real-time, as the raw data is
cumbersome to store and process in a flexible and easy manner. Therefore, we propose to use stream processing
with Kafka Streams. We were debating where to put the stream processing logic, either in the individual
services or in a separate service.

## Decision
We decided to use Kafka Streams for stream processing and to put the stream processing logic in a separate
service. This service will consume raw data from the factory and process it in real-time. The processed data
will then be sent to the respective factory station services via Kafka. A merged stream of data will be sent to
the monitoring service, which will then further process the data for analytical purposes.

## Consequences
By using a dedicated Kafka Streams service with a stream processing topology, we can process the data in 
real-time and send it to the respective factory station services. This way, we have centralized the stream
processing logic and can easily scale the service if needed. By sending the individual streams of data to the
respective factory station services, we can keep the services decoupled and ensure that each service is 
implemented in a lightweight manner. By sending a merged stream of data to the monitoring service, we can
further process the data for analytical purposes and gain insights into the factory's performance. This 
approach enables a flexible and scalable stream processing architecture that can be easily extended. Also, 
like this, we have achieved a nice separation of concerns between the stream processing logic and the 
actual factory station microservices.