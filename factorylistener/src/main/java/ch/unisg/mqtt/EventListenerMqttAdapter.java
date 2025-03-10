package ch.unisg.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import ch.unisg.messages.Message;
import ch.unisg.messages.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

/**
 * Adapter to convert MQTT messages to internal messages and send them to the message broker
 */
@Component
@RequiredArgsConstructor
public class EventListenerMqttAdapter implements MqttListener {

    private static final Logger logger = LoggerFactory.getLogger(EventListenerMqttAdapter.class);

    private final MessageSender messageSender;

    /**
     * This method handles an MQTT message.
     * It reads the payload of the message, creates a new Message object with the payload, and sends the message.
     * If an exception occurs during the processing of the message, it logs the error and returns false.
     * @param message the MQTT message to handle
     * @return true if the message was handled successfully, false otherwise
     */
    @Override
    public boolean handleEvent(MqttMessage message) {
        String payload = new String(message.getPayload());
        try {
            JsonNode data = new ObjectMapper().readTree(payload);

            Message<JsonNode> message1 = new Message<>(data);
            messageSender.send(message1);

        } catch (JsonProcessingException | NullPointerException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }
}