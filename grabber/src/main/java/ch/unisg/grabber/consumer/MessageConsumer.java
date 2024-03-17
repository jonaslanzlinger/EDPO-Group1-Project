package ch.unisg.grabber.consumer;

import ch.unisg.grabber.dto.GrabberUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    @KafkaListener(topics = "VGR_1")
    public void startMessageProcess(GrabberUpdateDto message){
        System.out.println(message.getData().getCurrent_pos_x());
    }

}
