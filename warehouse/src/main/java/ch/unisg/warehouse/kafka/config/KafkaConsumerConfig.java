package ch.unisg.warehouse.kafka.config;

import ch.unisg.warehouse.kafka.dto.WarehouseUpdateDto;
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
 * It uses Spring's @Configuration and @EnableKafka annotations to enable Kafka and mark this class as a source of bean definitions.
 * It also uses the @Value annotation to inject values from the application's properties file.
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
     * This method creates a ConsumerFactory bean that creates Kafka consumers.
     * @return A ConsumerFactory for WarehouseUpdateDto objects.
     */
    @Bean
    public ConsumerFactory<String, WarehouseUpdateDto> consumerFactory() {
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
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, WarehouseUpdateDto.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, trustedPackage);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * This method creates a ConcurrentKafkaListenerContainerFactory bean that creates Kafka listeners.
     * @return A ConcurrentKafkaListenerContainerFactory for WarehouseUpdateDto objects.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WarehouseUpdateDto>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, WarehouseUpdateDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}