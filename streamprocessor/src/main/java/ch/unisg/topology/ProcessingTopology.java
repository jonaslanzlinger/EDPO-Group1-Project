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
                (key, value) -> value.contains("VGR_1"),
                (key, value) -> value.contains("HBW_1")
        );


        // Write to the output topic
        stream.to("VGR_1-processed",
                Produced.with(
                        Serdes.ByteArray(),
                        Serdes.String()
                ));

        stream.to("HBW_1-processed",
                Produced.with(
                        Serdes.ByteArray(),
                        Serdes.String()
                ));





        return builder.build();
    }

}