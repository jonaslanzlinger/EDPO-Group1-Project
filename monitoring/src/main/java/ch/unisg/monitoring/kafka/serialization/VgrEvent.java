package ch.unisg.monitoring.kafka.serialization;

import ch.unisg.monitoring.domain.stations.VGR_1;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class VgrEvent {

    @SerializedName("id")
    String id;

    @SerializedName("source")
    String source;

    @SerializedName("data")
    VGR_1 data;

    @SerializedName("time")
    Instant time;

    @SerializedName("datacontenttype")
    String datacontenttype;

    @SerializedName("specversion")
    String specversion;


    @Override
    public String toString() {
        return "VgrEvent [id=" + id + ", source=" + source + ", time=" + time + ", data=" + data + ", " +
                "datacontenttype=" + datacontenttype + ", specversion=" + specversion + "]";
    }

}
