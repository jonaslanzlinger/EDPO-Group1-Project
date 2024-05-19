package ch.unisg.monitoring.kafka.topology;

import ch.unisg.monitoring.kafka.serialization.HbwEvent;
import ch.unisg.monitoring.kafka.serialization.VgrEvent;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FactoryStats {

    @SerializedName("VgrEvent")
    private VgrEvent vgrEvent;
    @SerializedName("HbwEvent")
    private HbwEvent hbwEvent;

    public FactoryStats(VgrEvent vgrEvent, HbwEvent hbwEvent) {
        this.vgrEvent = vgrEvent;
        this.hbwEvent = hbwEvent;
    }


    @Override
    public String toString() {
        return "FactoryStats{" +
                "vgrEvent=" + vgrEvent +
                ", hbwEvent=" + hbwEvent +
                '}';
    }
}