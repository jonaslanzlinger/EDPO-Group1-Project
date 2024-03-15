package ch.unisg.order.services;

import lombok.Getter;

@Getter
public class StartWarehouseCommandPayload {

    private String orderId;

    public StartWarehouseCommandPayload(String orderId) {
        this.orderId = orderId;
    }

}
