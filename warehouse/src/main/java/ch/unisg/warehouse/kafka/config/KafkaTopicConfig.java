package ch.unisg.warehouse.kafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a configuration class for Kafka topics.
 * It uses Spring's @Configuration annotation to mark this class as a source of bean definitions.
 * It also uses the @Value annotation to inject values from the application's properties file.
 */
@Configuration
public class KafkaTopicConfig {

    // The address of the Kafka bootstrap server
    @Value(value = "${kafka.bootstrap-address}")
    private String bootstrapAddress;

    /**
     * This method creates a KafkaAdmin bean that can be used to manage Kafka topics.
     * @return A KafkaAdmin instance.
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }
}