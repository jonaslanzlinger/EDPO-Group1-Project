package ch.unisg.monitoring.kafka.consumer;

import ch.unisg.monitoring.domain.MonitoringStore;
import ch.unisg.monitoring.kafka.dto.MonitorUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static ch.unisg.monitoring.utils.WorkflowLogger.info;

/**
 * Kafka consumer class that consumes messages from topic "monitoring".
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MonitorDataConsumer {

    private final MonitoringStore monitoringStore;

    /**
     * This method consumes messages from the Kafka topic "monitoring".
     * It receives a message from the topic and adds it to the MonitoringStore.
     *
     * @param message The message received from the Kafka topic.
     */
    @Transactional
    @KafkaListener(id = "order", topics = "monitoring")
    public void startMessageProcess(String message) {
        info(log, "MonitorDataConsumer", "Received message from Kafka topic: monitoring");
        MonitorUpdateDto monitorUpdateDto = MonitorUpdateDto.fromJson(message);
        info(log, "MonitorDataConsumer", "OrderId: " + monitorUpdateDto.getOrderId() + " Type: " + monitorUpdateDto.getType() + " Service: " + monitorUpdateDto.getService() + " Method: " + monitorUpdateDto.getMethod() + " Status: " + monitorUpdateDto.getStatus());
        monitoringStore.addMessage(monitorUpdateDto);
    }

}