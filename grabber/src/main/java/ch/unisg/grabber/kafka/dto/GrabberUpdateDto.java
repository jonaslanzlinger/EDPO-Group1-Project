package ch.unisg.grabber.kafka.dto;

import ch.unisg.grabber.domain.HBW_1;
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
public class GrabberUpdateDto implements Serializable {

    // The type of the Grabber update
    private String type;

    // The id of the Grabber update
    private String id;

    // The source of the Grabber update
    private String source;

    // The time of the Grabber update
    private String time;

    // The data of the Grabber update, represented as a VGR_1 object
    private HBW_1 data;

    // The content type of the data in the Grabber update
    private String datacontenttype;

    // The version of the specification used for the Grabber update
    private String specversion;
}