package ch.unisg.delivery.kafka.consumer;

import ch.unisg.delivery.domain.DeliveryStatusService;
import ch.unisg.delivery.kafka.dto.DeliveryUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * This is a consumer class that listens to Kafka messages.
 * It uses Spring's @Component annotation to mark this class as a component that can be autowired.
 * It also uses Lombok's @RequiredArgsConstructor annotation to automatically generate a constructor with required arguments.
 * It uses the @Slf4j annotation to enable logging.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    // The service that manages the delivery station status
    private final DeliveryStatusService deliveryStatusService;

    /**
     * This method is a Kafka listener that processes messages from the "VGR_1" topic.
     * It updates the delivery station status with the data from the message.
     * @param message The message from the Kafka topic.
     */
    @KafkaListener(topics = "VGR_1-processed")
    public void startMessageProcess(DeliveryUpdateDto message){
        deliveryStatusService.updateDeliveryStatus(message.getData());
    }

}
