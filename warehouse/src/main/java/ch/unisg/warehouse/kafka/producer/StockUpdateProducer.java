package ch.unisg.warehouse.kafka.producer;

import ch.unisg.warehouse.kafka.dto.StockUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class StockUpdateProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "warehouse";

    public void sendMessage(StockUpdateDto message) {
        kafkaTemplate.send(TOPIC, message);
    }
}