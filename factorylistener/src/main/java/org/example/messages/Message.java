package org.example.messages;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

public class Message<T> {

  // Cloud Events attributes (https://github.com/cloudevents/spec/blob/v1.0/spec.md)
  @Setter
  private String type;
  @Setter
  private String id = UUID.randomUUID().toString(); // unique id of this message
  private String source = "FactoryListener";
  @Setter
  @JsonFormat(shape = JsonFormat.Shape.STRING) // ISO-8601 compliant format
  private Instant time = Instant.now();
  @Setter
  private T data;
  private String datacontenttype="application/json";
  private String specversion="1.0";

  public Message() {    
  }
  
  public Message(String type, T payload) {
    this.type = type;
    this.data = payload;
  }

  @Override
  public String toString() {
    return "Message [type=" + type + ", id=" + id + ", time=" + time + ", data=" + data + "]";
  }

  public String getType() {
    return type;
  }

  public String getId() {
    return id;
  }

  public Instant getTime() {
    return time;
  }

  public T getData() {
    return data;
  }

  public String getSource() {
    return source;
  }

  public String getDatacontenttype() {
    return datacontenttype;
  }

  public String getSpecversion() {
    return specversion;
  }

}
