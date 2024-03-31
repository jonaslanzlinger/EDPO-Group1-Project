package ch.unisg.warehouse.kafka.producer;

import ch.unisg.warehouse.kafka.dto.WarehouseUpdateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageProducer {

    public static final String TOPIC_NAME = "warehouse";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public NewTopic autoCreateTopicOnStartupIfNotExistant() {
        return TopicBuilder.name(TOPIC_NAME).partitions(1).replicas(1).build();
    }

    public void send(WarehouseUpdateDto m) {
        try {
            // avoid too much magic and transform ourselves
            String jsonMessage = objectMapper.writeValueAsString(m);

            // wrap into a proper message for Kafka including a header
            ProducerRecord<String, String> record = new ProducerRecord<>("warehouse", jsonMessage);
            record.headers().add("type", m.getType().getBytes());

            // and send it
            kafkaTemplate.send(record);
        } catch (Exception e) {
            throw new RuntimeException("Could not transform and send message: "+ e.getMessage(), e);
        }
    }
}
