package ch.unisg.topology;

import ch.unisg.serialization.FactoryEvent;
import ch.unisg.serialization.json.FactoryEventSerdes;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;


public class ProcessingTopology {

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

        // Write to the output topic
        vgrStream.to("VGR_1-processed",
                Produced.with(
                        Serdes.ByteArray(),
                        new FactoryEventSerdes()
                ));

        hbwStream.to("HBW_1-processed",
                Produced.with(
                        Serdes.ByteArray(),
                        new FactoryEventSerdes()
                ));





        return builder.build();
    }

}
