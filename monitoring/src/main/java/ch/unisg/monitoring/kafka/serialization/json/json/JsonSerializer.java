package ch.unisg.monitoring.kafka.serialization.json.json;

import ch.unisg.monitoring.kafka.serialization.InstantTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;

/**
 * A Kafka serializer that uses JSON to serialize objects.
 * @param <T>
 */
class JsonSerializer<T> implements Serializer<T> {
  private final Gson gson =
      new GsonBuilder()
              .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
              .create();

  /** Default constructor needed by Kafka */
  public JsonSerializer() {}

  @Override
  public void configure(Map<String, ?> props, boolean isKey) {}

  @Override
  public byte[] serialize(String topic, T type) {
    return gson.toJson(type).getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public void close() {}
}