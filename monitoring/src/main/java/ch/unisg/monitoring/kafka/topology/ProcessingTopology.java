package ch.unisg.monitoring.kafka.topology;


import ch.unisg.monitoring.kafka.serialization.HbwEvent;
import ch.unisg.monitoring.kafka.serialization.VgrEvent;
import ch.unisg.monitoring.kafka.serialization.json.hbw.HbwEventSerdes;
import ch.unisg.monitoring.kafka.serialization.json.vgr.VgrEventSerdes;
import ch.unisg.monitoring.kafka.topology.aggregations.ColorStats;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;

import java.time.Duration;

import static ch.unisg.monitoring.kafka.serialization.json.json.JsonSerdes.jsonSerde;


public class ProcessingTopology {

    public static Topology build() {

        StreamsBuilder builder = new StreamsBuilder();
        Serde<ColorStats> colorStatsSerde = jsonSerde(ColorStats.class);
        HbwEventSerdes hbwEventSerdes = new HbwEventSerdes();
        VgrEventSerdes vgrEventSerdes = new VgrEventSerdes();

        KStream<String, byte[]> stream = builder.stream("monitoring-all", Consumed.with(Serdes.String(), Serdes.ByteArray()));

        // DEBUG
        // stream.print(Printed.<byte[], FactoryEvent>toSysOut().withLabel("monitoring-all"));
        // KStream<byte[], FactoryEvent> peekedStream = stream.peek((key, value) -> System.out.println("Key:
        // " + Arrays.toString(key)));

        KStream<String,  byte[]>[] branches = stream.branch(
                (key, value) -> key.equals("VGR_1"),
                (key, value) -> key.equals("HBW_1")
        );

        // DEBUG: check if the branch is working
        // branches[0].print(Printed.<byte[], FactoryEvent>toSysOut().withLabel("VGR_1"));
        // branches[1].print(Printed.<byte[], FactoryEvent>toSysOut().withLabel("HBW_1"));

        KStream<String, VgrEvent> vgrTypedStream =  branches[0].mapValues(v ->
                vgrEventSerdes.deserializer().deserialize("VGR_1",v)
        );

        KStream<String, HbwEvent> hbwTypedStream =  branches[1].mapValues(v ->
            hbwEventSerdes.deserializer().deserialize("HBW_1",v)
        );

        // DEBUG: print vgrTypedStream to console
        vgrTypedStream.print(Printed.<String, VgrEvent>toSysOut().withLabel("vgrTypedStream"));

        // DEBUG: print hbwTypedStream to console
        hbwTypedStream.print(Printed.<String, HbwEvent>toSysOut().withLabel("hbwTypedStream"));

        KStream<String, Double> colorSensorStream = vgrTypedStream.map((key, vgrEvent) ->
                new KeyValue<>(vgrEvent.getData().getColor(), vgrEvent.getData().getI8_color_sensor())
        );


        Initializer<ColorStats> aggregateInitializer = () -> new ColorStats(0,0,0);

        Aggregator<String, Double, ColorStats> aggregateAggregator = (key, value, colorStats) -> {
            long newTotalCount = colorStats.getTotalReadings() + 1;
            double newTotalOccurrences = colorStats.getTotalColorValues() + value;
            double newAverageColorVal = newTotalOccurrences / newTotalCount;
            return new ColorStats(newTotalCount,newTotalOccurrences,newAverageColorVal);
        };

        KTable<String, ColorStats> averageColorSensor =
                colorSensorStream
                        .groupBy((key, value) -> key, Grouped.with(Serdes.String(), Serdes.Double()))
                        .aggregate(
                                aggregateInitializer,
                                aggregateAggregator,
                                Materialized.<String, ColorStats, KeyValueStore<Bytes,byte[]>>
                                                as("colorStats")
                                        .withKeySerde(Serdes.String())
                                        .withValueSerde(colorStatsSerde));

        // Note:
        // The outputs of the windows only appear in the console when kafka commits the messages.
        // By default this is set to 30 seconds. After the first 30 seconds you can see 3 window outputs,
        // because we have set the window size to 10 seconds and grace period to 0 second.
        TimeWindows tumblingWindow = TimeWindows.ofSizeAndGrace(Duration.ofSeconds(10),
                Duration.ofSeconds(0));

        TimeWindowedKStream<String, VgrEvent> windowedVgr = vgrTypedStream
                .groupByKey()
                .windowedBy(tumblingWindow);

        TimeWindowedKStream<String, HbwEvent> windowedHbw = hbwTypedStream
                .groupByKey()
                .windowedBy(tumblingWindow);

        // Count the number of VGR_1 events in the tumbling window
        windowedVgr.count().toStream().foreach((key, count) -> System.out.println("Key: " + key.key() + ", Window: " + key.window() + ", Count: " + count));
        // Count the number of HBW_1 events in the tumbling window
        windowedHbw.count().toStream().foreach((key, count) -> System.out.println("Key: " + key.key() + ", Window: " + key.window() + ", Count: " + count));


        // Count the number of messages grouped by the color field.
        // Note: Also here the output appears only after the kafka commits the messages (30 seconds default).
        vgrTypedStream
                .groupBy((key, value) -> value.getData().getColor(),
                        Grouped.with(Serdes.String(),vgrEventSerdes))
                .count()
                .toStream()
                .foreach((key, count) -> System.out.println("Key: " + key + ", Count: " + count));



        return builder.build();
    }
}
