# FactoryListener Service
This service is responsible for listening on MQTT messages from the FactorySimulator service and 
forwarding them to the respective Kafka topics. For each station in the factory, a topic is created
and the microservices for the respective stations will consume the messages on their topics.

# MQTT
The service implements a [MqttClient](src%2Fmain%2Fjava%2Forg%2Fexample%2Fmqtt%2FMqttClient.java) that listens for messages on the topic "factory".
The [EventListenerMqttAdapter](src%2Fmain%2Fjava%2Forg%2Fexample%2Fmqtt%2FEventListenerMqttAdapter.java) 
is used for handling the messages and forwarding them to the respective Kafka topics.

# Kafka
Once a MQTT message has been received, the [MessageSender](src%2Fmain%2Fjava%2Forg%2Fexample%2Fmessages%2FMessageSender.java)
will send the message to the respective Kafka topic. The topic is named after the station that the message is intended for.

## Start sending data
To start sending data from the factory listener, we need to start the service. Then, we can call the
endpoint `http://localhost:8084/send` to start sending data. The [FactoryRestController](src%2Fmain%2Fjava%2Forg%2Fexample%2Frest%2FFactoryRestController.java)
is responsible for handling this request. Note: Kafka messages will only get sent if the FactorySimulator 
service is also sending messages.