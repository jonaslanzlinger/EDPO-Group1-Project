package ch.unisg.grabber.domain;

import lombok.Data;

@Data
public class Order {
    private String orderColor;
    private String orderId;
    private String deliveryMethod;
}
