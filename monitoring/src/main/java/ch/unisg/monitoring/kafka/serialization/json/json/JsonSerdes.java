package ch.unisg.monitoring.kafka.serialization.json.json;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

/**
 * Helper class to create JSON Serdes.
 */
public class JsonSerdes {

  public static <T> Serde<T> jsonSerde(Class<T> valueType) {
    JsonSerializer<T> serializer = new JsonSerializer<>();
    JsonDeserializer<T> deserializer = new JsonDeserializer<>(valueType);
    return Serdes.serdeFrom(serializer, deserializer);
  }
}