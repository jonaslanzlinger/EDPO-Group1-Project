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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;


public class ProcessingTopology {
    private static final Gson gsonHBW = new GsonBuilder()
            .registerTypeAdapter(HBW_1.class, new HbwDeserializer())
            .create();
    private static final Gson gsonVGR = new GsonBuilder()
            .registerTypeAdapter(VGR_1.class, new VgrDeserializer())
            .create();
    public static Topology build() {

        StreamsBuilder builder = new StreamsBuilder();


        builder.addStateStore(
                Stores.keyValueStoreBuilder(
                        Stores.inMemoryKeyValueStore("PreviousEventStore"),
                        Serdes.ByteArray(),
                        Serdes.String() // Assuming getData() returns a String
                )
        );


        KStream<byte[], FactoryEvent> stream =
                builder.stream("factory-all", Consumed.with(Serdes.ByteArray(), new FactoryEventSerdes()));

        stream.print(Printed.<byte[], FactoryEvent>toSysOut().withLabel("factory-all"));

        // filter out unused stations
        stream.filter((k, v) ->
            v.getData().toString().contains("VGR_1") || v.getData().toString().contains("HBW_1")
        );


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



        // Write to the output topic
        vgrTypedStream.to("VGR_1-processed",
                Produced.with(
                        Serdes.ByteArray(),
                        new VgrEventSerdes()
                ));

        hbwTypedStream.to("HBW_1-processed",
                Produced.with(
                        Serdes.ByteArray(),
                        new HbwEventSerdes()
                ));


        vgrTypedStream.mapValues(FactoryEvent::toFactory)
                .merge(hbwTypedStream.mapValues(FactoryEvent::toFactory))
                .to("monitoring-all",
                        Produced.with(Serdes.ByteArray(),
                                new FactoryEventSerdes()));


        return builder.build();
    }


}
