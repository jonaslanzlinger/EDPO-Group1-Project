package ch.unisg.order.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

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
  private String color;
  // Delivery method of the order
  private String deliveryMethod;
  // Current progress of the order
  private String progress;

  /**
   * Default constructor.
   * It initializes orderId with a random UUID.
   */
  public Order() {
    this.orderId = UUID.randomUUID().toString();
  }

  /**
   * Constructor with color parameter.
   * It initializes orderId with a random UUID and color with the provided color and deliveryMethod
   * @param color The color of the order.
   * @param deliveryMethod The delivery method of the order.
   */
  public Order(String color, String deliveryMethod) {
    this.orderId = UUID.randomUUID().toString();
    this.color = color;
    this.deliveryMethod = deliveryMethod;
    this.progress = "ordered";
  }
}