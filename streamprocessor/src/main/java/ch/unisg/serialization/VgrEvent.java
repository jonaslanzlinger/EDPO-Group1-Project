package ch.unisg.serialization;

import ch.unisg.domain.stations.VGR_1;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * This class is used to represent the event that is sent to the event bus.
 * It is used to serialize the event to JSON format.
 */
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
    String time;

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