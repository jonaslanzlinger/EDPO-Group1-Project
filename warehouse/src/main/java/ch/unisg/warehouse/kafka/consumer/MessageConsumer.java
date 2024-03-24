package ch.unisg.warehouse.kafka.consumer;

import ch.unisg.warehouse.kafka.dto.WarehouseUpdateDto;
import ch.unisg.warehouse.domain.WareHouseStatusService;
import ch.unisg.warehouse.utils.WorkflowLogger;
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

    // The service that manages the warehouse status
    private final WareHouseStatusService wareHouseStatusService;

    /**
     * This method is a Kafka listener that processes messages from the "HBW_1" topic.
     * It updates the warehouse status with the data from the message.
     * @param message The message from the Kafka topic.
     */
    @KafkaListener(topics = "HBW_1")
    public void startMessageProcess(WarehouseUpdateDto message){
        WorkflowLogger.info(log,"startMessageProcess", "Received message from Kafka topic: HBW_1");
        wareHouseStatusService.updateWarehouseStatus(message.getData());
    }

}