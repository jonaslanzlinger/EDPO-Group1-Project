package ch.unisg.monitoring.kafka.serialization.json.json;

public class CustomDeserializationException extends RuntimeException {

    public CustomDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }

}