package ch.unisg.monitoring.kafka.topology.aggregations;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * Class to store the aggregated statistics for a color.
 */
@Setter
@Getter
public class ColorStats {

    @SerializedName("ColorCount")
    private long totalReadings;
    @SerializedName("TotalColorValues")
    private double totalColorValues;
    @SerializedName("AverageColorValue")
    private double averageColorValue;

    public ColorStats() {
        totalReadings = 0;
        totalColorValues = 0;
        averageColorValue = 0;
    }

    public ColorStats(long totalReadings, double totalColorValues, double averageColorValue) {
        this.totalReadings = totalReadings;
        this.totalColorValues = totalColorValues;
        this.averageColorValue = averageColorValue;
    }

    public static ColorStats aggregate(String key, Double value, ColorStats agg) {
        long newTotalCount = agg.getTotalReadings() + 1;
        double newTotalOccurrences = agg.getTotalColorValues() + value;
        double newAverageColorVal = newTotalOccurrences / newTotalCount;
        return new ColorStats(newTotalCount,newTotalOccurrences,newAverageColorVal);
    }

    @Override
    public String toString() {
        return "ColorStats{" +
                "totalReadings=" + totalReadings +
                ", totalColorValues=" + totalColorValues +
                ", averageColorValue=" + averageColorValue +
                '}';
    }
}