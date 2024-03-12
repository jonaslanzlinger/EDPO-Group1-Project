package org.example.rest;

import org.example.domain.FactoryService;
import org.example.domain.stations.Station;
import org.example.messages.MessageSender;
import org.example.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;



@RestController
public class FactoryRestController {

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private FactoryService factoryService;

    @RequestMapping(path = "/send", method = GET)
    public String startSending() {

        List<Station> stations = factoryService.readFile("src/main/resources/data.txt");

        List<String> lines = factoryService.simpleRead("src/main/resources/data.txt");

        for (Station station : stations) {
            Message<Station> message = new Message<Station>("Event", station);
            messageSender.send(message);
        }


        // note that we cannot easily return an order id here - as everything is asynchronous
        // and blocking the client is not what we want.
        // but we return an own correlationId which can be used in the UI to show status maybe later
        return "Done";
    }

}