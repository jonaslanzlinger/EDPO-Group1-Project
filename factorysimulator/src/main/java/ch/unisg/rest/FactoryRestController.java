package ch.unisg.rest;

import ch.unisg.domain.stations.Station;
import org.eclipse.paho.client.mqttv3.MqttException;
import ch.unisg.domain.FactoryService;
import ch.unisg.mqtt.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * This class is a REST controller that provides an endpoint to start sending orders to the factory.
 */
@RestController
public class FactoryRestController {

    @Autowired
    private FactoryService factoryService;

    /**
     * This method is called when the /send endpoint is accessed. It reads the orders from a file and
     * sends them to the factory.
     *
     * @throws MqttException
     */
    @RequestMapping(path = "/send", method = GET)
    public String startSending() throws MqttException {

        List<Station> stations = factoryService.readFile("src/main/resources/data.txt");

        MqttClient mqttClient = MqttClient.getInstance(URI.create("tcp://mqtt:1883"));
        try {
            for (Station station : stations) {
                String jsonStation = factoryService.stationToJson(station);
                Thread.sleep(100);
                mqttClient.publish("factory", jsonStation);
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