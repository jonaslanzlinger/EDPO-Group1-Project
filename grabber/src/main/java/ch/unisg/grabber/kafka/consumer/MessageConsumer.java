package ch.unisg.grabber.kafka.consumer;

import ch.unisg.grabber.domain.GrabberStatusService;
import ch.unisg.grabber.kafka.dto.GrabberUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * This is a consumer class for Kafka messages.
 * It uses Spring's @Component annotation to indicate that it is a component class.
 * It uses Lombok's @RequiredArgsConstructor to generate a constructor with required properties.
 * It uses Lombok's @Slf4j to add a logger to the class.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    @Autowired
    private GrabberStatusService grabberStatusService;

    /**
     * This method starts the message process when a message is received on the "VGR_1" topic.
     * It uses Spring's @KafkaListener annotation to listen for messages on the "VGR_1" topic.
     * @param message The received message.
     */
    @KafkaListener(topics = "HBW_1-processed")
    public void startMessageProcess(GrabberUpdateDto message){
        grabberStatusService.setLatestStatus(message.getData());
    }

}