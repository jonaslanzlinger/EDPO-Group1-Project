package ch.unisg.order.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockUpdateConsumer {

    @KafkaListener(topics = "warehouse")
    public void startMessageProcess(StockDto message){
        System.out.println(message);
    }

}