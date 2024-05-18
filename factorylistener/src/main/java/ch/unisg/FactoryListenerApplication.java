package ch.unisg;

import ch.unisg.messages.MessageSender;
import ch.unisg.mqtt.EventListenerMqttAdapter;
import ch.unisg.mqtt.MqttClient;
import ch.unisg.mqtt.MqttDispatcher;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;


@SpringBootApplication
public class FactoryListenerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FactoryListenerApplication.class, args);
    }
}