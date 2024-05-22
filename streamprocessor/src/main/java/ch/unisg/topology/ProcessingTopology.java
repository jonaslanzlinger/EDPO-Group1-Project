package ch.unisg.topology;

import ch.unisg.domain.stations.HBW_1;
import ch.unisg.domain.stations.VGR_1;
import ch.unisg.serialization.FactoryEvent;
import ch.unisg.serialization.HbwEvent;
import ch.unisg.serialization.VgrEvent;
import ch.unisg.serialization.json.FactoryEventSerdes;
import ch.unisg.serialization.json.hbw.HbwDeserializer;
import ch.unisg.serialization.json.hbw.HbwEventSerdes;
import ch.unisg.serialization.json.vgr.VgrDeserializer;
import ch.unisg.serialization.json.vgr.VgrEventSerdes;
import ch.unisg.topology.util.PreviousEventFilterHBW;
import ch.unisg.topology.util.PreviousEventFilterVGR;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.nio.charset.StandardCharsets;


public class ProcessingTopology {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(HBW_1.class, new HbwDeserializer())
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

        KStream<byte[], FactoryEvent> stream =
                builder.stream("factory-all", Consumed.with(Serdes.ByteArray(), new FactoryEventSerdes()));

        //stream.print(Printed.<byte[], FactoryEvent>toSysOut().withLabel("factory-all"));

        // filter out unused stations
        stream.filter((k, v) ->
            v.getData().toString().contains("VGR_1") || v.getData().toString().contains("HBW_1")
        );

        var branches = stream.split(Named.as("branch-"))
                .branch((k, v) -> v.getData().toString().contains("VGR_1"),Branched.as("vgr1"))
                .branch((k, v) -> v.getData().toString().contains("HBW_1"), Branched.as("hbw1"))
                .defaultBranch(Branched.as("other"));


        // Adjusts keys so that they reflect the station
        KStream<byte[], FactoryEvent> vgrEventRekeyedStream = branches.get("branch-vgr1").selectKey((oldKey, value) ->
                "VGR_1".getBytes(StandardCharsets.UTF_8));
        KStream<byte[], FactoryEvent> hbwEventRekeyedStream = branches.get("branch-hbw1").selectKey((oldKey, value) ->
                "HBW_1".getBytes(StandardCharsets.UTF_8));

        // set correct typing
        KStream<byte[], VgrEvent> vgrTypedStream =  vgrEventRekeyedStream.mapValues(v -> {
            VgrEvent vgrEvent = new VgrEvent();
            vgrEvent.setId(v.getId());
            vgrEvent.setSource(v.getSource());
            vgrEvent.setTime(v.getTime());
            vgrEvent.setDatacontenttype(v.getDatacontenttype());
            vgrEvent.setSpecversion(v.getSpecversion());
            // Deserialize the data field to VGR_1
            String jsonData = gson.toJson(v.getData());
            VGR_1 vgrData = gson.fromJson(jsonData, VGR_1.class);
            vgrEvent.setData(vgrData);
            return vgrEvent;
        });



        // set correct typing
        KStream<byte[], HbwEvent> hbwTypedStream =  hbwEventRekeyedStream.mapValues(v -> {
            HbwEvent hbwEvent = new HbwEvent();
            hbwEvent.setId(v.getId());
            hbwEvent.setTime(v.getTime());
            hbwEvent.setSpecversion(v.getSpecversion());
            hbwEvent.setSource(v.getSource());
            hbwEvent.setDatacontenttype(v.getDatacontenttype());
            // Deserialize the data field to HBW_1
            String jsonData = gson.toJson(v.getData());
            HBW_1 hbwData = gson.fromJson(jsonData, HBW_1.class);
            hbwEvent.setData(hbwData);
            return hbwEvent;
        });

        // custom filtering logic to only let events through that differs from the previous one
        KStream<byte[], VgrEvent>  vgrTypedFilteredStream =  vgrTypedStream
                .transform(PreviousEventFilterVGR::new, "previous-event-store-vgr");

        KStream<byte[], HbwEvent>  hbwTypedFilteredStream =  hbwTypedStream
                .transform(PreviousEventFilterHBW::new, "previous-event-store-hbw");

        vgrTypedFilteredStream.print(Printed.<byte[], VgrEvent>toSysOut().withLabel("VGR_1-processed"));
        hbwTypedFilteredStream.print(Printed.<byte[], HbwEvent>toSysOut().withLabel("HBW_1-processed"));

        // Write to the output topic
        vgrTypedFilteredStream.to("VGR_1-processed",
                Produced.with(
                        Serdes.ByteArray(),
                        new VgrEventSerdes()
                ));

        hbwTypedFilteredStream.to("HBW_1-processed",
                Produced.with(
                        Serdes.ByteArray(),
                        new HbwEventSerdes()
                ));

        vgrTypedStream.mapValues(FactoryEvent::toFactory)
                .merge(hbwTypedStream.mapValues(FactoryEvent::toFactory))
                .to("monitoring-all",
                        Produced.with(Serdes.ByteArray(),
                                new FactoryEventSerdes()));

        // DEBUG
        // Print both streams to the console
        //vgrTypedStream.mapValues(FactoryEvent::toFactory)
        //        .merge(hbwTypedStream.mapValues(FactoryEvent::toFactory))
        //        .print(Printed.<byte[], FactoryEvent>toSysOut().withLabel("monitoring-all"));


        return builder.build();
    }


}
