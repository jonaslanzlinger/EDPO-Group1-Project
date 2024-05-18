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
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;


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

        // DEBUG
        // stream.print(Printed.<byte[], FactoryEvent>toSysOut().withLabel("monitoring-all"));
        // KStream<byte[], FactoryEvent> peekedStream = stream.peek((key, value) -> System.out.println("Key:
        // " + Arrays.toString(key)));

        KStream<byte[], FactoryEvent>[] branches = stream.branch(
                (key, value) -> Arrays.equals(key, "VGR_1".getBytes(StandardCharsets.UTF_8)),
                (key, value) -> Arrays.equals(key, "HBW_1".getBytes(StandardCharsets.UTF_8))
        );

        // DEBUG: check if the branch is working
        // branches[0].print(Printed.<byte[], FactoryEvent>toSysOut().withLabel("VGR_1"));
        // branches[1].print(Printed.<byte[], FactoryEvent>toSysOut().withLabel("HBW_1"));

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

        // DEBUG: print vgrTypedStream to console
         vgrTypedStream.print(Printed.<byte[], VgrEvent>toSysOut().withLabel("vgrTypedStream"));

        // DEBUG: print hbwTypedStream to console
         hbwTypedStream.print(Printed.<byte[], HbwEvent>toSysOut().withLabel("hbwTypedStream"));


        // Define a hopping window of 10 seconds with a 5 seconds advance
        TimeWindows hoppingWindow = TimeWindows.of(Duration.ofSeconds(10)).advanceBy(Duration.ofSeconds(5));

        // Count the number of VGR_1 events in the hopping window
        KTable<Windowed<byte[]>, Long> vgrCount =
                vgrTypedStream
                        .groupByKey()
                        .windowedBy(hoppingWindow)
                        .count(Materialized.as("vgr-count-store"))
                        .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded().shutDownWhenFull()));

        // Count the number of HBW_1 events in the hopping window
        KTable<Windowed<byte[]>, Long> hbwCount =
                hbwTypedStream
                        .groupByKey()
                        .windowedBy(hoppingWindow)
                        .count(Materialized.as("hbw-count-store"))
                        .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded().shutDownWhenFull()));

        vgrCount.toStream().print(Printed.<Windowed<byte[]>, Long>toSysOut().withLabel("vgr-count-store"));
        hbwCount.toStream().print(Printed.<Windowed<byte[]>, Long>toSysOut().withLabel("hbw-count-store"));



        return builder.build();
    }


}
