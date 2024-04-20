package ch.unisg.monitoring.kafka.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * This is a Data Transfer Object (DTO) class for Monitoring updates.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonitorUpdateDto implements Serializable {

    /* The type of the message (Event, Command) */
    private String type;

    /* The ID of the order to monitor */
    private String orderId;

    /* The method that was called by a service */
    private String method;

    /* The status of the method call (success, failed) */
    private String status;

    /* The service that called the method */
    private String service;

    public static MonitorUpdateDto fromJson(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(message, MonitorUpdateDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJson(MonitorUpdateDto message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}