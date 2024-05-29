package ch.unisg.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static ch.unisg.utils.Utility.*;

/**
 * Helper to send messages, currently nailed to Kafka, but could also send via AMQP (e.g. RabbitMQ) or
 * any other transport easily
 */
@Component
@NoArgsConstructor
public class MessageSender {

  // The topic to send messages to
  private static final String TOPIC = "factory-all";

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  /**
   * Send a message to the configured topic
   * @param m the message to send
   */
  public void send(Message<?> m) {
    try {
      // avoid too much magic and transform ourselves
      String jsonMessage = objectMapper.writeValueAsString(m);

      // wrap into a proper message for Kafka including a header
      ProducerRecord<String, String> record = new ProducerRecord<String, String>(TOPIC,"mqtt",jsonMessage);

      logInfo("send", "Sending message to topic: "
              + TOPIC + " with key: " + record.key());

      // ... and send it
      kafkaTemplate.send(record);
    } catch (Exception e) {
      throw new RuntimeException("Could not transform and send message: "+ e.getMessage(), e);
    }
  }
}
