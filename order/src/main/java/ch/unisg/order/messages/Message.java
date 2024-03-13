package ch.unisg.order.messages;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Message<T> {

  // Cloud Events attributes (https://github.com/cloudevents/spec/blob/v1.0/spec.md)
  private String type;
  private String id = UUID.randomUUID().toString(); // unique id of this message
  private String source = "Order";
  @JsonFormat(shape = JsonFormat.Shape.STRING) // ISO-8601 compliant format  
  private Instant time = Instant.now();
  private T data;
  private String dataContentType = "application/json";
  private String specVersion = "1.0";
  
  // Extension attributes
  private String traceId = UUID.randomUUID().toString(); // trace id, default: new unique
  private String correlationId; // id which can be used for correlation later if required
  private String group = "order";

  public Message(String type, T payload) {
    this.type = type;
    this.data = payload;
  }
  
  public Message(String type, String traceId, T payload) {
    this.type = type;
    this.traceId = traceId;
    this.data = payload;
  }

  @Override
  public String toString() {
    return "Message [type=" + type + ", id=" + id + ", time=" + time + ", data=" + data + ", correlationId=" + correlationId + ", tradeId=" + traceId + "]";
  }
}
