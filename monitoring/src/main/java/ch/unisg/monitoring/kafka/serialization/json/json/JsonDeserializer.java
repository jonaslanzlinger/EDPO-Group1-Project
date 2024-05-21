package ch.unisg.monitoring.kafka.serialization.json.json;


import ch.unisg.monitoring.kafka.serialization.InstantTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.kafka.common.serialization.Deserializer;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Map;

public class JsonDeserializer<T> implements Deserializer<T> {
  private final Gson gson =
      new GsonBuilder()
              .registerTypeAdapter(Instant.class,new InstantTypeAdapter())
              .create();

  private Class<T> destinationClass;
  private Type reflectionTypeToken;

  /** Default constructor needed by Kafka */
  public JsonDeserializer(Class<T> destinationClass) {
    this.destinationClass = destinationClass;
  }

  public JsonDeserializer(Type reflectionTypeToken) {
    this.reflectionTypeToken = reflectionTypeToken;
  }

  @Override
  public void configure(Map<String, ?> props, boolean isKey) {}

  @Override
  public T deserialize(String topic, byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    Type type = destinationClass != null ? destinationClass : reflectionTypeToken;
    try {
      return gson.fromJson(new String(bytes), type);
    } catch (JsonSyntaxException e) {
      throw new CustomDeserializationException("Failed to deserialize data for topic: " + topic, e);
    }
  }


  @Override
  public void close() {}
}
