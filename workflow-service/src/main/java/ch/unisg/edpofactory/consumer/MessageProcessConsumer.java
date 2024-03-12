package ch.unisg.edpofactory.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ch.unisg.edpofactory.dto.CamundaMessageDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageProcessConsumer {

    private final MessageService messageService;
    private final static String MESSAGE_START = "EDPOFactory";
    private final static String MESSAGE_INTERMEDIATE = "MessageIntermediate";

    @KafkaListener(topics = "EDPOFactory")
    public void startMessageProcess(CamundaMessageDto message){
        System.out.println("Received message: " + message.getDto());
        messageService.correlateMessage(message, MESSAGE_START);
    }

    @KafkaListener(topics = "intermediate-message-topic")
    public void listen(CamundaMessageDto camundaMessageDto){
       messageService.correlateMessage(camundaMessageDto, MESSAGE_INTERMEDIATE);
    }
}
