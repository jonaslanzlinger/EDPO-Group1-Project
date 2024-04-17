package ch.unisg.order.kafka.producer;


import ch.unisg.order.kafka.dto.MonitorUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MonitorDataProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "monitoring";
    private static final String SERVICE = "order";
    private static final String EVENT = "Event";

    public enum MonitorStatus {
        success, failed
    }

    public void sendMessage(MonitorUpdateDto message) {
        kafkaTemplate.send(TOPIC, message);
    }

    public void sendMonitorUpdate(String orderId, String method, String status) {
        MonitorUpdateDto monitorUpdateDto = MonitorUpdateDto.builder()
                .orderId(orderId)
                .method(method)
                .status(status)
                .service(SERVICE)
                .type(EVENT)
                .build();
        sendMessage(monitorUpdateDto);
    }

}