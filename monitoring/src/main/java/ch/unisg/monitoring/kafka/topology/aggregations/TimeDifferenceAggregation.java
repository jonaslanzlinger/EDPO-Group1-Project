package ch.unisg.monitoring.kafka.topology.aggregations;

import ch.unisg.monitoring.kafka.serialization.HbwEvent;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class TimeDifferenceAggregation {
    @SerializedName("FirstTimestamp")
    private Instant firstTimestamp;
    @SerializedName("LastTimestamp")
    private Instant lastTimestamp;


    public TimeDifferenceAggregation add(HbwEvent event) {
        Instant timestamp = Instant.parse(event.getTime());
        if (firstTimestamp == null || timestamp.isBefore(firstTimestamp)) {
            firstTimestamp = timestamp;
        }
        if (lastTimestamp == null || timestamp.isAfter(lastTimestamp)) {
            lastTimestamp = timestamp;
        }
        return this;
    }

    public TimeDifferenceAggregation add(TimeDifferenceAggregation agg2) {
        if (firstTimestamp == null || agg2.firstTimestamp.isBefore(firstTimestamp)) {
            firstTimestamp = agg2.firstTimestamp;
        }
        if (lastTimestamp == null || agg2.lastTimestamp.isAfter(lastTimestamp)) {
            lastTimestamp = agg2.lastTimestamp;
        }
        return this;
    }

    public long getTimeDifference() {
        if (firstTimestamp != null && lastTimestamp != null) {
            return lastTimestamp.toEpochMilli() - firstTimestamp.toEpochMilli();
        }
        return 0;
        }

    @Override
    public String toString() {
        return "TimeDifference{" +
                "firstTimestamp=" + firstTimestamp +
                ", lastTimestamp=" + lastTimestamp +
                '}';
    }
}
