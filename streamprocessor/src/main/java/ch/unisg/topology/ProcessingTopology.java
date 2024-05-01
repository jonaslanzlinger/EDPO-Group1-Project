package ch.unisg.topology;

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

        // TODO here (look at the example gaze project)
        KStream<byte[], String> stream =
                builder.stream("factory-all", Consumed.with(Serdes.ByteArray(), Serdes.String()));
        stream.print(Printed.<byte[], String>toSysOut().withLabel("FACTORY-ALL"));


        KStream<byte[], String>[] branches = stream.branch(
                (key, value) -> value.contains("station=VGR_1"),
                (key, value) -> value.contains("station=HBW_1")
        );

        KStream<byte[], String> vgrStream = branches[0];
        KStream<byte[], String> hbwStream = branches[1];


        // Write to the output topic
        vgrStream.to("VGR_1-processed",
                Produced.with(
                        Serdes.ByteArray(),
                        Serdes.String()
                ));

        hbwStream.to("HBW_1-processed",
                Produced.with(
                        Serdes.ByteArray(),
                        Serdes.String()
                ));





        return builder.build();
    }

}
