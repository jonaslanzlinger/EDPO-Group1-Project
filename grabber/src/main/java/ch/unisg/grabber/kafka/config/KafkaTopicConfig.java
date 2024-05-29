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
}