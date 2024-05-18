package ch.unisg.monitoring.kafka.topology;

import ch.unisg.monitoring.domain.stations.HBW_1;
import ch.unisg.monitoring.domain.stations.VGR_1;

import ch.unisg.monitoring.serialization.FactoryEvent;
import ch.unisg.monitoring.serialization.HbwEvent;
import ch.unisg.monitoring.serialization.VgrEvent;
import ch.unisg.monitoring.serialization.json.FactoryEventSerdes;
import ch.unisg.monitoring.serialization.json.hbw.HbwDeserializer;
import ch.unisg.monitoring.serialization.json.vgr.VgrDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.kafka.support.serializer.JsonSerde;


public class ProcessingTopology {
    private static final Gson gsonHBW = new GsonBuilder()
            .registerTypeAdapter(HBW_1 .class, new HbwDeserializer())
            .create();
    private static final Gson gsonVGR = new GsonBuilder()
            .registerTypeAdapter(VGR_1.class, new VgrDeserializer())
            .create();
    public static Topology build() {

        StreamsBuilder builder = new StreamsBuilder();

        StoreBuilder<KeyValueStore<byte[], VgrEvent>> storeBuilderVGR =
                Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore("previous-event-store-vgr"),
                        Serdes.ByteArray(),
                        new JsonSerde<>(VgrEvent.class)
                );
        StoreBuilder<KeyValueStore<byte[], HbwEvent>> storeBuilderHBW =
                Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore("previous-event-store-hbw"),
                        Serdes.ByteArray(),
                        new JsonSerde<>(HbwEvent.class)
                );


        builder.addStateStore(storeBuilderVGR);
        builder.addStateStore(storeBuilderHBW);

        KStream<byte[], FactoryEvent> stream = builder.stream("monitoring-all", Consumed.with(Serdes.ByteArray(), new FactoryEventSerdes()));

        stream.print(Printed.<byte[], FactoryEvent>toSysOut().withLabel("monitoring-all"));

        stream.filter((k, v) -> v.getData().toString().contains("VGR_1") || v.getData().toString().contains("HBW_1"));


        // branch the stream
        KStream<byte[], FactoryEvent>[] branches = stream.branch(
                (key, value) -> value.getData().toString().contains("VGR_1"),
                (key, value) -> value.getData().toString().contains("HBW_1")
        );

        KStream<byte[], VgrEvent> vgrTypedStream =  branches[0].mapValues(v -> {
            VgrEvent vgrEvent = new VgrEvent();
            vgrEvent.setId(v.getId());
            vgrEvent.setSource(v.getSource());
            vgrEvent.setTime(v.getTime());
            vgrEvent.setDatacontenttype(v.getDatacontenttype());
            vgrEvent.setSpecversion(v.getSpecversion());
            // Deserialize the data field to VGR_1
            String jsonData = gsonVGR.toJson(v.getData());
            VGR_1 vgrData = gsonVGR.fromJson(jsonData, VGR_1.class);
            vgrEvent.setData(vgrData);
            return vgrEvent;
        });


        KStream<byte[], HbwEvent> hbwTypedStream =  branches[1].mapValues(v -> {
            HbwEvent hbwEvent = new HbwEvent();
            hbwEvent.setId(v.getId());
            hbwEvent.setTime(v.getTime());
            hbwEvent.setSpecversion(v.getSpecversion());
            hbwEvent.setSource(v.getSource());
            hbwEvent.setDatacontenttype(v.getDatacontenttype());
            // Deserialize the data field to HBW_1
            String jsonData = gsonHBW.toJson(v.getData());
            HBW_1 hbwData = gsonHBW.fromJson(jsonData, HBW_1.class);
            hbwEvent.setData(hbwData);

            return hbwEvent;
        });

        return builder.build();
    }


}
