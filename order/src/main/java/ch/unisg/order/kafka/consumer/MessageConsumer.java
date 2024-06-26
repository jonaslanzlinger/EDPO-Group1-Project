package ch.unisg.order.kafka.consumer;

import ch.unisg.order.domain.Stock;
import ch.unisg.order.kafka.dto.StockUpdateDto;
import ch.unisg.order.utils.WorkflowLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class listens to the Kafka topic "warehouse" and processes the messages received.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    private final Stock stock;

    @Transactional
    @KafkaListener(id = "order", topics = "warehouse")
    public void startMessageProcess(String message){
        WorkflowLogger.info(log, "OrderMessageConsumer", "Received message from Kafka topic: warehouse");
        StockUpdateDto stockUpdateDto = StockUpdateDto.fromJson(message);
        stock.updateWarehouseStatus(stockUpdateDto.getData());
    }
}