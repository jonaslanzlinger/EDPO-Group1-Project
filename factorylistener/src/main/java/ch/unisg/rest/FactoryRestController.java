package ch.unisg.rest;

import ch.unisg.mqtt.MqttDispatcher;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;


import ch.unisg.mqtt.MqttClient;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


import static org.springframework.web.bind.annotation.RequestMethod.GET;



@RestController
@RequiredArgsConstructor
public class FactoryRestController {

    private final MqttDispatcher mqttDispatcher;


    @RequestMapping(path = "/send", method = GET)
    public String startSending() {

        MqttClient mqttClient = MqttClient.getInstance(URI.create("tcp://mqtt:1883"), mqttDispatcher);

        try {
            mqttClient.connect();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

        return "Done";
    }

}