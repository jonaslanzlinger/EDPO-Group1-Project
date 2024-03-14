package org.example.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class EventListenerMqttAdapter implements MqttListener {

    private static final Logger logger = LoggerFactory.getLogger(EventListenerMqttAdapter.class);



    @Override
    public boolean handleEvent(MqttMessage message) {
        String payload = new String(message.getPayload());
        logger.info("Factory Update: " + payload);


        try {
            JsonNode data = new ObjectMapper().readTree(payload);

            String auctionId = data.get("auctionId").asText();
            String bidderName = data.get("bidderName").asText();
            String bidderAuctionHouseUri = data.get("auctionHouseUri").asText();
            String taskListUri = data.get("taskListUri").asText();


        } catch (JsonProcessingException | NullPointerException e) {
            logger.error(e.getMessage(), e);
            return false;
        }

        return true;
    }
}