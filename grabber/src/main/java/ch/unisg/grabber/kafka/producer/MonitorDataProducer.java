package ch.unisg.grabber.kafka.producer;

import ch.unisg.grabber.kafka.dto.MonitorUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MonitorDataProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "monitoring";

    public void sendMessage(MonitorUpdateDto message) {
        kafkaTemplate.send(TOPIC, message);
    }

}