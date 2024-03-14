package org.example.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Interface for MQTT listeners
 */
interface MqttListener {

    boolean handleEvent(MqttMessage message);
}