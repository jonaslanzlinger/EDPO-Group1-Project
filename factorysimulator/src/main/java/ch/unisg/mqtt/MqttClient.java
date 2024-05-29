package ch.unisg.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * This class is a wrapper around the Eclipse Paho MQTT client. It provides a simple interface to
 * connect to an MQTT broker, publish messages, and subscribe to topics.
 */
public class MqttClient {

    private static MqttClient factoryClient = null;

    private org.eclipse.paho.client.mqttv3.MqttClient mqttClient;
    private final String mqttClientId;
    private final URI brokerAddress;

    private MqttClient(URI brokerAddress) {
        this.mqttClientId = UUID.randomUUID().toString();
        this.brokerAddress = brokerAddress;
        // The MQTT client is initialized when either connect() or publishMessage() are invoked
        this.mqttClient = null;
    }

    public static synchronized MqttClient getInstance(URI brokerAddress) {
        if (factoryClient == null) {
            factoryClient = new MqttClient(brokerAddress);
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
}