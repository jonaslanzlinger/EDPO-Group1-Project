package ch.unisg.warehouse.domain;

import lombok.Data;

/**
 * This is a data class that represents an order in the warehouse.
 */
@Data
public class Order {
    private String orderColor;
    private String orderId;
    private String deliveryMethod;
}
