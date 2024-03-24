package ch.unisg.grabber.kafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a configuration class for Kafka topics.
 * It uses Spring's @Configuration annotation to indicate that it is a configuration class.
 */
@Configuration
public class KafkaTopicConfig {

    // The address of the Kafka bootstrap server
    @Value(value = "${kafka.bootstrap-address}")
    private String bootstrapAddress;

    /**
     * This method creates a KafkaAdmin.
     * It sets the bootstrap servers for the KafkaAdmin.
     * @return A KafkaAdmin with the configured bootstrap servers.
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    // The following beans are commented out, but they would create new Kafka topics if uncommented.

    /*
    @Bean
    public NewTopic startProcessMessageTopic() {
        return new NewTopic("start-process-message-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic intermediateMessageTopic() {
        return new NewTopic("intermediate-message-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic serviceTaskMessageTopic() {
        return new NewTopic("service-task-message-topic", 1, (short) 1);
    }
    */
}