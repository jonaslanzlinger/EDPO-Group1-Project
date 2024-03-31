package ch.unisg.order.kafka;

import ch.unisg.order.util.WorkflowLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    @KafkaListener(topics = "warehouse")
    public void startMessageProcess(StockDto message){
        WorkflowLogger.info(log,"OrderMessageConsumer", "Received message from Kafka topic: warehouse");
        System.out.println(message.getData());
    }

}