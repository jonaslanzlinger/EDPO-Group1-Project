# Streamprocessor Service

The streamprocessor service is responsible for processing station events that have been emitted by the single stations.
It then sends these events to the monitoring service for further processing.

# Domain

The domain contains two classes that represent the stations data
structures [Stations](./src/main/java/ch/unisg/domain/stations).
The [ProcessingTopology](./src/main/java/ch/unisg/topology/ProcessingTopology.java) class represents the topology with
the different streams and processors.
The [CustomTimestampExtractor](./src/main/java/ch/unisg/serialization/timestampExtractors/CustomTimestampExtractor.java)
class is used to extract the timestamp from the station events.

# Configuration

No configuration is needed for the streamprocessor service.
It runs as part of the root docker compose file.


