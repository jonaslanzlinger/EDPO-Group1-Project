package ch.unisg.delivery.kafka.config;

import ch.unisg.delivery.kafka.dto.DeliveryUpdateDto;
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
 * KafkaConsumerConfig is a configuration class for Kafka consumer.
 * It is annotated with @EnableKafka and @Configuration to indicate that it is a configuration class and to enable Kafka.
 */
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${kafka.bootstrap-address}")
    private String bootstrapAddress;

    @Value(value = "${kafka.group-id}")
    private String groupId;

    @Value(value = "${kafka.trusted-packages}")
    private String trustedPackage;

    /**
     * This method creates a ConsumerFactory for Kafka.
     * It sets up the necessary configurations such as bootstrap servers, group id, key deserializer, value deserializer, etc.
     * @return a ConsumerFactory for Kafka
     */
    @Bean
    public ConsumerFactory<String, DeliveryUpdateDto> consumerFactory() {
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
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, DeliveryUpdateDto.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, trustedPackage);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * This method creates a ConcurrentKafkaListenerContainerFactory for Kafka.
     * It sets the ConsumerFactory created by the consumerFactory() method.
     * @return a ConcurrentKafkaListenerContainerFactory for Kafka
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DeliveryUpdateDto>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, DeliveryUpdateDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}