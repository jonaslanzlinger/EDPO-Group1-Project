package ch.unisg.serialization.json.hbw;

import ch.unisg.serialization.FactoryEvent;
import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

public class HbwEventSerializer implements Serializer<FactoryEvent> {

    private final Gson gson = new Gson();

    @Override
    public byte[] serialize(String topic, FactoryEvent factoryEvent) {
        if (factoryEvent == null) return null;
        return gson.toJson(factoryEvent).getBytes(StandardCharsets.UTF_8);
    }

}
