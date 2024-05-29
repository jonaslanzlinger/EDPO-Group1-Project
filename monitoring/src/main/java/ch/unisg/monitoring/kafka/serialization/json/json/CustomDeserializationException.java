package ch.unisg.monitoring.kafka.serialization.json.json;

/**
 * This class is a custom exception for deserialization errors.
 */
public class CustomDeserializationException extends RuntimeException {

    public CustomDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }

}