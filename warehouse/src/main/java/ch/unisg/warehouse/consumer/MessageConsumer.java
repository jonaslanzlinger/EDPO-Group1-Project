package ch.unisg.warehouse.consumer;

import ch.unisg.warehouse.dto.WarehouseUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    @KafkaListener(topics = "HBW_1")
    public void startMessageProcess(WarehouseUpdateDto message){
        System.out.println(message.getData().getCurrent_pos_x());
    }

}
