package ch.unisg.grabber.kafka.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a configuration class for Kafka producers.
 * It uses Spring's @Configuration annotation to indicate that it is a configuration class.
 */
@Configuration
public class KafkaProducerConfig {

    // The address of the Kafka bootstrap server
    @Value(value = "${kafka.bootstrap-address}")
    private String bootstrapAddress;

    /**
     * This method creates a ProducerFactory for String.
     * It sets the bootstrap servers, key serializer, value serializer and trusted packages for the producer.
     * @return A ProducerFactory for String.
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    /**
     * This method creates a KafkaTemplate for String.
     * It sets the ProducerFactory for the KafkaTemplate.
     * @return A KafkaTemplate for String.
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}