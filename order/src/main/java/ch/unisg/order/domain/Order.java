package ch.unisg.order.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
public class Order implements Serializable {
  private String orderId;
    private String color;

    public Order() {
      this.orderId = UUID.randomUUID().toString();
    }

    public Order(String color) {
      this.orderId = UUID.randomUUID().toString();
      this.color = color;
    }

}

