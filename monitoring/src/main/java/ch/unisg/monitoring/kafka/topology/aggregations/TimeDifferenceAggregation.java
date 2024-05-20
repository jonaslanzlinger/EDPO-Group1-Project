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

    public static TimeDifferenceAggregation add(String Key, HbwEvent event, TimeDifferenceAggregation agg) {
        Instant newTime = event.getTime();

        Instant newFirstTimestamp = (agg.firstTimestamp == null || newTime.isBefore(agg.firstTimestamp)) ? newTime : agg.firstTimestamp;
        Instant newLastTimestamp = (agg.lastTimestamp == null || newTime.isAfter(agg.lastTimestamp)) ? newTime : agg.lastTimestamp;

        return new TimeDifferenceAggregation(newFirstTimestamp, newLastTimestamp);
    }

    public static TimeDifferenceAggregation merge(String Key, TimeDifferenceAggregation agg1, TimeDifferenceAggregation agg2) {
        Instant newFirstTimestamp;
        Instant newLastTimestamp;

        if (agg1.firstTimestamp != null && agg2.firstTimestamp != null) {
            newFirstTimestamp = agg1.firstTimestamp.isBefore(agg2.firstTimestamp) ? agg1.firstTimestamp : agg2.firstTimestamp;
        } else if (agg1.firstTimestamp != null) {
            newFirstTimestamp = agg1.firstTimestamp;
        } else {
            newFirstTimestamp = agg2.firstTimestamp;
        }

        if (agg1.lastTimestamp != null && agg2.lastTimestamp != null) {
            newLastTimestamp = agg1.lastTimestamp.isAfter(agg2.lastTimestamp) ? agg1.lastTimestamp : agg2.lastTimestamp;
        } else if (agg1.lastTimestamp != null) {
            newLastTimestamp = agg1.lastTimestamp;
        } else {
            newLastTimestamp = agg2.lastTimestamp;
        }

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
