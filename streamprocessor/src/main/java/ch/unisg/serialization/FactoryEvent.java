package ch.unisg.serialization;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

public class FactoryEvent {

    @Getter
    @Setter
    @SerializedName("id")
    String id;

    @Getter
    @Setter
    @SerializedName("source")
    String source;

    @Getter
    @Setter
    @SerializedName("time")
    String time;

    @Getter
    @Setter
    @SerializedName("data")
    Object data;

    @Getter
    @Setter
    @SerializedName("datacontenttype")
    String datacontenttype;

    @Getter
    @Setter
    @SerializedName("specversion")
    String specversion;

    @Override
    public String toString() {
        return "FactoryEvent [id=" + id + ", source=" + source + ", time=" + time + ", data=" + data + ", " +
                "datacontenttype=" + datacontenttype + ", specversion=" + specversion + "]";
    }

}
