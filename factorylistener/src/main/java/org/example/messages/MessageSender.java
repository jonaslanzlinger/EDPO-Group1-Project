package org.example.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Helper to send messages, currently nailed to Kafka, but could also send via AMQP (e.g. RabbitMQ) or
 * any other transport easily
 */
@Component
@NoArgsConstructor
public class MessageSender {

  public enum STATIONS {
    VGR_1, MM_1, HBW_1, EC_1, SM_1, OV_1, WT_1
  }

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Bean
  public void autoCreateTopicOnStartupIfNotExistant() {
    for (STATIONS station : STATIONS.values()) {
      TopicBuilder.name(station.toString()).partitions(1).replicas(1).build();
    }

  }

  public void send(Message<?> m) {
    try {
      // avoid too much magic and transform ourselves
      String jsonMessage = objectMapper.writeValueAsString(m);

      // wrap into a proper message for Kafka including a header
      ProducerRecord<String, String> record = new ProducerRecord<String, String>(m.getType(), jsonMessage);
      record.headers().add("type", m.getType().getBytes());
      System.out.println("Sending message to topic: " + m.getType());

      // and send it
      kafkaTemplate.send(record);
    } catch (Exception e) {
      throw new RuntimeException("Could not transform and send message: "+ e.getMessage(), e);
    }
  }
}
