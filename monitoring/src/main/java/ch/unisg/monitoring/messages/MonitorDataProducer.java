package ch.unisg.monitoring.messages;

import ch.unisg.monitoring.kafka.dto.MonitorUpdateDto;
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