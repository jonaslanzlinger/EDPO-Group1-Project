package ch.unisg.monitoring.kafka.topology;

import ch.unisg.monitoring.kafka.serialization.HbwEvent;
import ch.unisg.monitoring.kafka.serialization.VgrEvent;

import ch.unisg.monitoring.kafka.serialization.json.hbw.HbwEventSerdes;
import ch.unisg.monitoring.kafka.serialization.json.vgr.VgrEventSerdes;
import org.apache.kafka.common.serialization.Serdes;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;


public class ProcessingTopology {
    public static Topology build() {

        StreamsBuilder builder = new StreamsBuilder();

        Consumed<byte[], byte[]> consumedWithTimestampExtractor = Consumed.with(Serdes.ByteArray(), Serdes.ByteArray());

        KStream<byte[], byte[]> stream = builder.stream("monitoring-all", consumedWithTimestampExtractor);

        // DEBUG
        // stream.print(Printed.<byte[], byte[]>toSysOut().withLabel("monitoring-all"));
        // KStream<byte[], byte[]> peekedStream = stream.peek((key, value) -> System.out.println("Key:
        // " + Arrays.toString(key)));

        KStream<byte[], byte[]>[] branches = stream.branch(
                (key, value) -> Arrays.equals(key, "VGR_1".getBytes(StandardCharsets.UTF_8)),
                (key, value) -> Arrays.equals(key, "HBW_1".getBytes(StandardCharsets.UTF_8))
        );

        // DEBUG: check if the branch is working
        // branches[0].print(Printed.<byte[], FactoryEvent>toSysOut().withLabel("VGR_1"));
        // branches[1].print(Printed.<byte[], FactoryEvent>toSysOut().withLabel("HBW_1"));
        HbwEventSerdes hbwEventSerdes = new HbwEventSerdes();
        VgrEventSerdes vgrEventSerdes = new VgrEventSerdes();


        KStream<byte[], VgrEvent> vgrTypedStream =  branches[0].mapValues(v ->
                vgrEventSerdes.deserializer().deserialize("VGR_1", v)
        );

        KStream<byte[], HbwEvent> hbwTypedStream =  branches[1].mapValues(v ->
                hbwEventSerdes.deserializer().deserialize("HBW_1", v)
        );


        // DEBUG: print vgrTypedStream to console
        vgrTypedStream.print(Printed.<byte[], VgrEvent>toSysOut().withLabel("vgrTypedStream"));

        // DEBUG: print hbwTypedStream to console
        hbwTypedStream.print(Printed.<byte[], HbwEvent>toSysOut().withLabel("hbwTypedStream"));


        // Note:
        // The outputs of the windows only appear in the console when kafka commits the messages.
        // By default this is set to 30 seconds. After the first 30 seconds you can see 3 window outputs,
        // because we have set the window size to 10 seconds and grace period to 0 second.
        TimeWindows tumblingWindow = TimeWindows.ofSizeAndGrace(Duration.ofSeconds(10),
                Duration.ofSeconds(0));

        TimeWindowedKStream<byte[], VgrEvent> windowedVgr = vgrTypedStream
                .groupByKey()
                .windowedBy(tumblingWindow);

        TimeWindowedKStream<byte[], HbwEvent> windowedHbw = hbwTypedStream
                .groupByKey()
                .windowedBy(tumblingWindow);

        // Count the number of VGR_1 events in the tumbling window
        windowedVgr.count().toStream().foreach((key, count) -> System.out.println("Key: " + new String(key.key(),
                StandardCharsets.UTF_8) + ", Window: " + key.window() + ", Count: " + count));
        // Count the number of HBW_1 events in the tumbling window
        windowedHbw.count().toStream().foreach((key, count) -> System.out.println("Key: " + new String(key.key(),
                StandardCharsets.UTF_8) + ", Window: " + key.window() + ", Count: " + count));


        // Count the number of messages grouped by the color field.
        // Note: Also here the output appears only after the kafka commits the messages (30 seconds default).
        vgrTypedStream
                .groupBy((key, value) -> value.getData().getColor(),
                        Grouped.with(Serdes.String(), vgrEventSerdes))
                .count()
                .toStream()
                .foreach((key, count) -> System.out.println("Key: " + key + ", Count: " + count));



        return builder.build();
    }
}
