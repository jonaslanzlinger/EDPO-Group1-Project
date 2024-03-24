package ch.unisg.grabber.kafka.config;

import ch.unisg.grabber.kafka.dto.GrabberUpdateDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a configuration class for Kafka consumers.
 * It uses Spring's @EnableKafka and @Configuration annotations to enable Kafka and indicate that it is a configuration class.
 */
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    // The address of the Kafka bootstrap server
    @Value(value = "${kafka.bootstrap-address}")
    private String bootstrapAddress;

    // The group ID for the Kafka consumer
    @Value(value = "${kafka.group-id}")
    private String groupId;

    // The trusted packages for the Kafka consumer
    @Value(value = "${kafka.trusted-packages}")
    private String trustedPackage;

    /**
     * This method creates a ConsumerFactory for GrabberUpdateDto.
     * It sets the bootstrap servers, group ID, key deserializer, value deserializer, default value type and trusted packages for the consumer.
     * @return A ConsumerFactory for GrabberUpdateDto.
     */
    @Bean
    public ConsumerFactory<String, GrabberUpdateDto> consumerFactory() {
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
                JsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, GrabberUpdateDto.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, trustedPackage);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * This method creates a ConcurrentKafkaListenerContainerFactory for GrabberUpdateDto.
     * It sets the ConsumerFactory for the listener container factory.
     * @return A ConcurrentKafkaListenerContainerFactory for GrabberUpdateDto.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GrabberUpdateDto>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, GrabberUpdateDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}