package ch.unisg.order.kafka.config;

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
     * This method creates a KafkaAdmin which is responsible for managing and inspecting topics, brokers and other Kafka objects.
     * It sets the bootstrap servers for the admin client.
     * @return A KafkaAdmin for managing and inspecting Kafka objects.
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }
}