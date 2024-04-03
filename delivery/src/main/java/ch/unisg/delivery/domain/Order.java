package ch.unisg.delivery.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


/**
 * This is a domain class for Order.
 * It uses Lombok's @Data annotation to generate getters, setters, equals, hashCode and toString methods.
 * It uses Lombok's @Builder annotation to provide a builder pattern for object creation.
 * It uses Lombok's @AllArgsConstructor to generate a constructor with all properties.
 * It implements Serializable interface to support serialization.
 */
@Data
@Builder
@AllArgsConstructor
public class Order implements Serializable {
  // Unique identifier for the order
  private String orderId;
  // Color of the order
  private String orderColor;
    // Delivery method for the order
  private String deliveryMethod;
}