package ch.unisg.monitoring.messages;

import ch.unisg.monitoring.domain.MonitoringStore;
import ch.unisg.monitoring.kafka.dto.MonitorUpdateDto;
import ch.unisg.monitoring.utils.WorkflowLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    private final MonitoringStore monitoringStore;

    @Transactional
    @KafkaListener(id = "order", topics = "monitoring")
    public void startMessageProcess(String message) {
        WorkflowLogger.info(log, "MonitorDataConsumer", "Received message from Kafka topic: monitoring");
        MonitorUpdateDto monitorUpdateDto = MonitorUpdateDto.fromJson(message);
        WorkflowLogger.info(log, "MonitorDataConsumer", "OrderId: " + monitorUpdateDto.getOrderId() + " Type: " + monitorUpdateDto.getType() + " Service: " + monitorUpdateDto.getService() + " Method:" + monitorUpdateDto.getMethod() + " Status: " + monitorUpdateDto.getStatus());
        monitoringStore.addMessage(monitorUpdateDto);
    }

}