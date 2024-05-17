package ch.unisg.serialization;

import ch.unisg.domain.stations.HBW_1;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HbwEvent {

    @SerializedName("id")
    String id;

    @SerializedName("source")
    String source;

    @SerializedName("time")
    String time;

    @SerializedName("data")
    HBW_1 data;

    @SerializedName("datacontenttype")
    String datacontenttype;

    @SerializedName("specversion")
    String specversion;

    @Override
    public String toString() {
        return "HbwEvent [id=" + id + ", source=" + source + ", time=" + time + ", data=" + data + ", " +
                "datacontenttype=" + datacontenttype + ", specversion=" + specversion + "]";
    }

}
