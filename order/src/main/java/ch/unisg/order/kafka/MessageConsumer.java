package ch.unisg.order.kafka;


import ch.unisg.order.domain.Stock;
import ch.unisg.order.util.WorkflowLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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