package ch.unisg.serialization;

import ch.unisg.domain.stations.HBW_1;
import ch.unisg.domain.stations.VGR_1;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

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
        return "FactoryEvent [id=" + id + ", source=" + source + ", time=" + time + ", data=" + data + ", " +
                "datacontenttype=" + datacontenttype + ", specversion=" + specversion + "]";
    }

}
