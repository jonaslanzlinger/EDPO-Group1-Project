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
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;


public class ProcessingTopology {
    private static final Gson gsonHBW = new GsonBuilder()
            .registerTypeAdapter(HBW_1.class, new HbwDeserializer())
            .create();
    private static final Gson gsonVGR = new GsonBuilder()
            .registerTypeAdapter(VGR_1.class, new VgrDeserializer())
            .create();
    public static Topology build() {

        StreamsBuilder builder = new StreamsBuilder();

        KStream<byte[], FactoryEvent> stream =
                builder.stream("factory-all", Consumed.with(Serdes.ByteArray(), new FactoryEventSerdes()));

        stream.print(Printed.<byte[], FactoryEvent>toSysOut().withLabel("factory-all"));

        // Create the respective Data Types
        KStream<byte[], FactoryEvent> typedStream = stream.mapValues(
                (factoryEvent) -> {
                    FactoryEvent typedFactoryEvent = new FactoryEvent();
                    typedFactoryEvent.setId(factoryEvent.getId());
                    typedFactoryEvent.setSource(factoryEvent.getSource());
                    typedFactoryEvent.setTime(factoryEvent.getTime());
                    typedFactoryEvent.setData(factoryEvent.getData());
                    typedFactoryEvent.setDatacontenttype(factoryEvent.getDatacontenttype());
                    typedFactoryEvent.setSpecversion(factoryEvent.getSpecversion());
                    return typedFactoryEvent;
                });

        // branch the stream
        KStream<byte[], FactoryEvent>[] branches = typedStream.branch(
                (key, value) -> value.getData().toString().contains("VGR_1"),
                (key, value) -> value.getData().toString().contains("HBW_1")
        );

        KStream<byte[], FactoryEvent> vgrStream = branches[0];
        KStream<byte[], FactoryEvent> hbwStream = branches[1];

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

        typedStream.to("monitoring-all",
                Produced.with(
                        Serdes.ByteArray(),
                        new FactoryEventSerdes()
                ));

        return builder.build();
    }

}
