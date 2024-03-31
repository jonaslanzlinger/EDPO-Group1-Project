package ch.unisg.order.kafka.config;

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
 * This is a configuration class for Kafka producer.
 * It uses Spring's @Configuration annotation to indicate that it is a configuration class.
 */
@Configuration
public class KafkaProducerConfig {

    // The address of the Kafka bootstrap server
    @Value(value = "${kafka.bootstrap-address}")
    private String bootstrapAddress;

    /**
     * This method creates a ProducerFactory which is responsible for creating Kafka producers.
     * It sets the bootstrap servers, and key and value serializers.
     * @return A ProducerFactory for creating Kafka producers.
     */
    @Bean
    public ProducerFactory<String, String> producerFactory() {
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
     * This method creates a KafkaTemplate.
     * It sets the ProducerFactory for the template.
     * The KafkaTemplate wraps a Producer instance and provides convenience methods for sending messages to Kafka topics.
     * @return A KafkaTemplate for sending messages to Kafka topics.
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}