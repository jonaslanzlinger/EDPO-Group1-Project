package ch.unisg.monitoring.kafka.serialization;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FactoryEvent {

    @SerializedName("id")
    String id;

    @SerializedName("source")
    String source;
    @SerializedName("data")
    Object data;

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
    public static FactoryEvent toFactory(HbwEvent v) {
        FactoryEvent event = new FactoryEvent();
        event.setData(v.getData());
        event.setId(v.getId());
        event.setDatacontenttype(v.getDatacontenttype());
        event.setSource(v.getSource());
        event.setSpecversion(v.getSpecversion());
        event.setTime(v.getTime());
        return event;
    }

    public static FactoryEvent toFactory(VgrEvent v) {
        FactoryEvent event = new FactoryEvent();
        event.setData(v.getData());
        event.setId(v.getId());
        event.setDatacontenttype(v.getDatacontenttype());
        event.setSource(v.getSource());
        event.setSpecversion(v.getSpecversion());
        event.setTime(v.getTime());
        return event;
    }
}
