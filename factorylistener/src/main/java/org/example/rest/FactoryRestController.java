package org.example.rest;

import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;


import org.example.messages.MessageSender;

import org.example.mqtt.EventListenerMqttAdapter;
import org.example.mqtt.MqttClient;
import org.example.mqtt.MqttDispatcher;

import org.springframework.beans.factory.annotation.Autowired;
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