package org.example.rest;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.example.domain.FactoryService;
import org.example.domain.stations.Station;
import org.example.mqtt.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;



@RestController
public class FactoryRestController {


    @Autowired
    private FactoryService factoryService;



    @RequestMapping(path = "/send", method = GET)
    public String startSending() throws MqttException {

        List<Station> stations = factoryService.readFile("src/main/resources/data.txt");

        // List<String> lines = factoryService.simpleRead("src/main/resources/data.txt");

        MqttClient mqttClient = MqttClient.getInstance(URI.create("tcp://mqtt:1883"));
        try {
            for (Station station : stations) {
                if (station.getStation().equals("VGA_1") || station.getStation().equals("HBW_1")) {
                    String jsonStation = factoryService.stationToJson(station);
                    Thread.sleep(1000);
                    mqttClient.publish("factory", jsonStation);
                }
            }
        } catch (MqttException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            mqttClient.disconnect();
        }
        // note that we cannot easily return an order id here - as everything is asynchronous
        // and blocking the client is not what we want.
        // but we return an own correlationId which can be used in the UI to show status maybe later
        return "Done";
    }

}