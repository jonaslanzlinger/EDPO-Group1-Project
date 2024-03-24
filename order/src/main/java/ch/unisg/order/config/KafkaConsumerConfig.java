package ch.unisg.order.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a configuration class for Kafka consumer.
 * It uses Spring's @Configuration annotation to indicate that it is a configuration class.
 * It uses Spring's @EnableKafka annotation to enable detection of @KafkaListener annotation on spring managed beans.
 */
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    // The address of the Kafka bootstrap server
    @Value(value = "${kafka.bootstrap-address}")
    private String bootstrapAddress;

    // The group ID of the Kafka consumer
    @Value(value = "${kafka.group-id}")
    private String groupId;

    /**
     * This method creates a ConsumerFactory which is responsible for creating Kafka consumers.
     * It sets the bootstrap servers, group id, and key and value deserializers.
     * @return A ConsumerFactory for creating Kafka consumers.
     */
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                groupId);
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * This method creates a ConcurrentKafkaListenerContainerFactory.
     * It sets the ConsumerFactory for the container.
     * The ConcurrentKafkaListenerContainerFactory is responsible for creating containers for methods annotated with @KafkaListener.
     * @return A ConcurrentKafkaListenerContainerFactory for creating Kafka listener containers.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}