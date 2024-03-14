package org.example.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.UUID;


public class MqttClient {
    private static final Logger LOGGER = LogManager.getLogger(MqttClient.class);

    private static MqttClient factoryClient = null;

    private org.eclipse.paho.client.mqttv3.MqttClient mqttClient;
    private final String mqttClientId;
    private final URI brokerAddress;

    private final MessageReceivedCallback messageReceivedCallback;

    private final MqttDispatcher dispatcher;


    private MqttClient(URI brokerAddress, MqttDispatcher dispatcher) {
        this.mqttClientId = UUID.randomUUID().toString();
        this.brokerAddress = brokerAddress;

        // The MQTT client is initialized when either connect() or publishMessage() are invoked
        this.mqttClient = null;
        this.dispatcher = dispatcher;
        this.messageReceivedCallback = new MessageReceivedCallback();
    }

    public static synchronized MqttClient getInstance(URI brokerAddress, MqttDispatcher dispatcher) {

        if (factoryClient == null) {
            factoryClient = new MqttClient(brokerAddress, dispatcher);
        }
        return factoryClient;
    }

    public static synchronized MqttClient getInstance() {

        if (factoryClient == null) {
            throw new IllegalStateException("MqttClient has not been initialized yet");
        }

        return factoryClient;
    }

    public void connect() throws MqttException {
        mqttClient = new org.eclipse.paho.client.mqttv3.MqttClient(brokerAddress.toASCIIString(), mqttClientId, new MemoryPersistence());
        mqttClient.connect();
        mqttClient.setCallback(messageReceivedCallback);

        subscribeToAllRegisteredTopics();
    }

    public void disconnect() throws MqttException {
        mqttClient.disconnect();
    }

    public void publish(String topic, String message) throws MqttException {
        if (mqttClient == null) {
            connect();
        }

        MqttMessage mqttMessage = new MqttMessage(message.getBytes(StandardCharsets.UTF_8));
        mqttClient.publish(topic, mqttMessage);
    }

    public void subscribeToTopic(String topic) throws MqttException {
        if (mqttClient == null) {
            connect();
        }

        mqttClient.subscribe(topic);
    }

    private void subscribeToAllRegisteredTopics() throws MqttException {
        for (String topic : dispatcher.getAllTopics()) {
            subscribeToTopic(topic);
        }
    }

    private class MessageReceivedCallback implements MqttCallback {

        @Override
        public void connectionLost(Throwable cause) {  }

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            if (topic != null && !topic.isEmpty()) {
                dispatcher.dispatchEvent(topic, message);
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {  }
    }
}