package ch.unisg.warehouse.services;

import lombok.Getter;

@Getter
public class FinishWarehouseCommandPayload {

    private String orderId;

    public FinishWarehouseCommandPayload(String orderId) {
        this.orderId = orderId;
    }

}
