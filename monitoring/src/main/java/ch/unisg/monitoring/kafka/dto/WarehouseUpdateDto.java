package ch.unisg.monitoring.kafka.dto;

import ch.unisg.monitoring.domain.HBW_1;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * This is a data transfer object (DTO) class for warehouse updates.
 * It implements Serializable to allow it to be sent over the network.
 * It uses Lombok's @Data annotation to automatically generate getters, setters, equals, hashCode, and toString methods.
 * It uses Lombok's @Builder annotation to automatically generate a builder for this class.
 * It uses Lombok's @AllArgsConstructor and @NoArgsConstructor annotations to automatically generate constructors with and without parameters.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseUpdateDto implements Serializable {

    // The type of the update
    private String type;

    // The ID of the update
    private String id;

    // The source of the update
    private String source;

    // The time of the update
    private String time;

    // The data of the update
    private HBW_1 data;

    // The content type of the data
    private String datacontenttype;

    // The version of the specification
    private String specversion;
}