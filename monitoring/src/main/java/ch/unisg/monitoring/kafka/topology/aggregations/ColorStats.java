package ch.unisg.monitoring.kafka.topology.aggregations;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ColorStats {

    @SerializedName("ColorCount")
    private long colorCount;
    @SerializedName("TotalColorValues")
    private double totalColorValues;
    @SerializedName("AverageColorValue")
    private double averageColorValue;

    public ColorStats(long colorCount, double totalColorValues, double averageColorValue) {
        this.colorCount = colorCount;
        this.totalColorValues = totalColorValues;
        this.averageColorValue = averageColorValue;
    }


    @Override
    public String toString() {
        return "ColorStats{" +
                "colorCount=" + colorCount +
                ", totalColorValues=" + totalColorValues +
                ", averageColorValue=" + averageColorValue +
                '}';
    }
}