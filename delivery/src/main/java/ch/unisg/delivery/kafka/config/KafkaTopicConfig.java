package ch.unisg.delivery.kafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * KafkaTopicConfig is a configuration class for Kafka topics.
 * It is annotated with @Configuration to indicate that it is a configuration class.
 */
@Configuration
public class KafkaTopicConfig {

    @Value(value = "${kafka.bootstrap-address}")
    private String bootstrapAddress;

    /**
     * The kafkaAdmin() method creates a KafkaAdmin bean that is used to create topics in Kafka.
     * The method is annotated with @Bean to indicate that it is a bean configuration method.
     * @return KafkaAdmin
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }
}