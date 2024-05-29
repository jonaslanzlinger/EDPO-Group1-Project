package ch.unisg.messages;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Message is a generic class for creating messages with various data types.
 * It includes attributes based on the Cloud Events specification (https://github.com/cloudevents/spec/blob/v1.0/spec.md).
 */
public class Message<T> {

  // Unique id of this message
  @Setter
  private String id = UUID.randomUUID().toString();

  // Source of the message
  private String source = "FactoryListener";

  // Time of the message creation in ISO-8601 compliant format
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Instant time = Instant.now();

  // Data of the message
  @Setter
  private T data;

  // Content type of the data
  private String datacontenttype="application/json";

  // Version of the specification
  private String specversion="1.0";

  // Default constructor
  public Message() {
  }

  // Constructor with payload
  public Message(T payload) {
    this.data = payload;
  }

  @Override
    public String toString() {
        return "Message [id=" + id + ", source=" + source + ", time=" + time + ", data=" + data + ", datacontenttype=" + datacontenttype + ", specversion=" + specversion + "]";
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
