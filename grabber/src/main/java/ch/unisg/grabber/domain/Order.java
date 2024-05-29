package ch.unisg.grabber.domain;

import lombok.Data;

/**
 * Order class represents an order in the system.
 */
@Data
public class Order {
    private String orderColor;
    private String orderId;
    private String deliveryMethod;
}
