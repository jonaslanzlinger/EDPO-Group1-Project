package ch.unisg.order.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * This is a Data Transfer Object (DTO) class for Grabber updates.
 * It uses Lombok's @Data annotation to generate getters, setters, equals, hashCode and toString methods.
 * It uses Lombok's @Builder annotation to provide a builder pattern for object creation.
 * It uses Lombok's @AllArgsConstructor to generate a constructor with all properties.
 * It uses Lombok's @NoArgsConstructor to generate a no-args constructor.
 * It implements Serializable interface to allow this object to be converted into a byte stream.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonitorUpdateDto implements Serializable {

    private String type;

    private String orderId;

    private String method;

    private String status;

    private String service;
}