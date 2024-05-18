package ch.unisg.topology.util;

import ch.unisg.serialization.VgrEvent;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;


public class PreviousEventFilterVGR implements Transformer<byte[], VgrEvent, KeyValue<byte[], VgrEvent>> {

    private KeyValueStore<byte[], VgrEvent> stateStore;
    private ProcessorContext context;

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        this.stateStore = context.getStateStore("previous-event-store-vgr");
    }

    @Override
    public KeyValue<byte[], VgrEvent> transform(byte[] key, VgrEvent value) {
        VgrEvent previousEvent = stateStore.get(key);

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

    private boolean shouldFilter(VgrEvent previousEvent, VgrEvent currentEvent) {
        return previousEvent.getData().getColor().equals(currentEvent.getData().getColor());
    }
}
