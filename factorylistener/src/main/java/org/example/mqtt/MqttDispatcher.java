package org.example.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

@Component
public class MqttDispatcher {

    private final Map<String, MqttListener> router;

    private final EventListenerMqttAdapter eventListenerMqttAdapter;

    public MqttDispatcher(
        EventListenerMqttAdapter eventListenerMqttAdapter) {
        this.router = new Hashtable<>();
        this.eventListenerMqttAdapter = eventListenerMqttAdapter;

        initRouter();
    }

    private void initRouter() {
        // TODO: mqtt topic

        router.put("FTFactory/HBW_1",eventListenerMqttAdapter);
        router.put("FTFactory/VGR_1",eventListenerMqttAdapter);
    }

    /**
     * Returns all topics registered with this dispatcher.
     *
     * @return the set of registered topics
     */
    public Set<String> getAllTopics() {
        return router.keySet();
    }

    /**
     * Dispatches an event received via MQTT for a given topic.
     *
     * @param topic   the topic for which the MQTT message was received
     * @param message the received MQTT message
     */
    public void dispatchEvent(String topic, MqttMessage message) {
        MqttListener listener = router.get(topic);
        listener.handleEvent(message);
    }
}