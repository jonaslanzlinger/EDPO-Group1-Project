package ch.unisg.delivery.kafka.consumer;

import ch.unisg.delivery.kafka.dto.DeliveryUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    @KafkaListener(topics = "VGR_1")
    public void startMessageProcess(DeliveryUpdateDto message){
        System.out.println(message.getData().getCurrent_pos_x());
    }

}
