package ch.unisg.topology.util;

import ch.unisg.serialization.HbwEvent;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;


public class PreviousEventFilterHBW implements Transformer<byte[], HbwEvent, KeyValue<byte[], HbwEvent>> {

    private KeyValueStore<byte[], HbwEvent> stateStore;
    private ProcessorContext context;

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        this.stateStore = context.getStateStore("previous-event-store-hbw");
    }

    @Override
    public KeyValue<byte[], HbwEvent> transform(byte[] key, HbwEvent value) {
        HbwEvent previousEvent = stateStore.get(key);

        if (previousEvent != null) {
            // Apply filtering logic based on the current and previous event
            if (shouldFilter(previousEvent, value)) {
                // Update the state store with the current event
                stateStore.put(key, value);
                return null;  // Filter out this record
            }
        }

        // Update the state store with the current event
        stateStore.put(key, value);
        return new KeyValue<>(key, value);  // Do not filter out this record
    }

    @Override
    public void close() {
        // Close resources if needed
    }

    private boolean shouldFilter(HbwEvent previousEvent, HbwEvent currentEvent) {
        var noChangeInStock = previousEvent.getData().getCurrent_stock().equals(currentEvent.getData().getCurrent_stock());
        var noChangeInSensors = previousEvent.getData().isI4_light_barrier() == currentEvent.getData().isI4_light_barrier() &&
                previousEvent.getData().isI1_light_barrier() == currentEvent.getData().isI1_light_barrier();
        return noChangeInSensors && noChangeInStock;
    }
}
