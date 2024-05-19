package ch.unisg.monitoring.kafka.topology.aggregations;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ColorStats {

    @SerializedName("ColorCount")
    private long totalReadings;
    @SerializedName("TotalColorValues")
    private double totalColorValues;
    @SerializedName("AverageColorValue")
    private double averageColorValue;

    public ColorStats(long totalReadings, double totalColorValues, double averageColorValue) {
        this.totalReadings = totalReadings;
        this.totalColorValues = totalColorValues;
        this.averageColorValue = averageColorValue;
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