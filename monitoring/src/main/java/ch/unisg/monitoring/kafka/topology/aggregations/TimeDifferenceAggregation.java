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

    public TimeDifferenceAggregation() {
        this.firstTimestamp = null;
        this.lastTimestamp = null;
    }

    public TimeDifferenceAggregation(Instant firstTimestamp, Instant lastTimestamp) {
        this.firstTimestamp = firstTimestamp;
        this.lastTimestamp = lastTimestamp;
    }

    public TimeDifferenceAggregation add(HbwEvent event) {
        Instant timestamp = Instant.parse(event.getTime());

        Instant newFirstTimestamp = (firstTimestamp == null || timestamp.isBefore(firstTimestamp)) ? timestamp : firstTimestamp;
        Instant newLastTimestamp = (lastTimestamp == null || timestamp.isAfter(lastTimestamp)) ? timestamp : lastTimestamp;

        return new TimeDifferenceAggregation(newFirstTimestamp, newLastTimestamp);
    }

    public TimeDifferenceAggregation add(TimeDifferenceAggregation agg2) {
        if (agg2 == null) return this;

        Instant newFirstTimestamp = (firstTimestamp == null || (agg2.firstTimestamp != null && agg2.firstTimestamp.isBefore(firstTimestamp))) ? agg2.firstTimestamp : firstTimestamp;
        Instant newLastTimestamp = (lastTimestamp == null || (agg2.lastTimestamp != null && agg2.lastTimestamp.isAfter(lastTimestamp))) ? agg2.lastTimestamp : lastTimestamp;

        return new TimeDifferenceAggregation(newFirstTimestamp, newLastTimestamp);
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
