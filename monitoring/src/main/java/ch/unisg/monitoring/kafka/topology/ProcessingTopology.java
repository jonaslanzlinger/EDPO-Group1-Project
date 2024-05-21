package ch.unisg.monitoring.kafka.topology;

import ch.unisg.monitoring.kafka.topology.aggregations.FactoryStats;
import ch.unisg.monitoring.kafka.topology.aggregations.TimeDifferenceAggregation;
import org.apache.kafka.common.serialization.Serde;


import ch.unisg.monitoring.kafka.serialization.HbwEvent;
import ch.unisg.monitoring.kafka.serialization.VgrEvent;
import ch.unisg.monitoring.kafka.serialization.json.hbw.HbwEventSerdes;
import ch.unisg.monitoring.kafka.serialization.json.vgr.VgrEventSerdes;
import ch.unisg.monitoring.kafka.topology.aggregations.ColorStats;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.SessionStore;

import java.time.Duration;


import static ch.unisg.monitoring.kafka.serialization.json.json.JsonSerdes.jsonSerde;


public class ProcessingTopology {

    public static Topology build() {

        StreamsBuilder builder = new StreamsBuilder();

        Serde<ColorStats> colorStatsSerde = jsonSerde(ColorStats.class);
        Serde<TimeDifferenceAggregation> timeDifferenceAggregationSerde = jsonSerde(TimeDifferenceAggregation.class);
        HbwEventSerdes hbwEventSerdes = new HbwEventSerdes();
        VgrEventSerdes vgrEventSerdes = new VgrEventSerdes();
        Serde<FactoryStats> factoryStatsSerde = jsonSerde(FactoryStats.class);

        KStream<String, byte[]> stream = builder.stream("monitoring-all", Consumed.with(Serdes.String(), Serdes.ByteArray()));

        var branches = stream.split(Named.as("branch-"))
                .branch((k, v) -> k.equals("VGR_1"),Branched.as("vgr1"))
                .branch((k, v) -> k.equals("HBW_1"), Branched.as("hbw1"))
                .defaultBranch(Branched.as("other"));

        KStream<String, VgrEvent> vgrTypedStream = branches.get("branch-vgr1").mapValues(v ->
                vgrEventSerdes.deserializer().deserialize("VGR_1",v)
        );

        KStream<String, HbwEvent> hbwTypedStream =  branches.get("branch-hbw1").mapValues(v ->
                hbwEventSerdes.deserializer().deserialize("HBW_1",v)
        );

        // Create Stream of Color, ColorValues
        KStream<String, Double> colorSensorStream = vgrTypedStream.map((key, vgrEvent) ->
                new KeyValue<>(vgrEvent.getData().getColor(), vgrEvent.getData().getI8_color_sensor())
        );

        // KTable that exposes the ColorStats for each color
        colorSensorStream
                .groupBy((key, value) -> key, Grouped.with(Serdes.String(), Serdes.Double()))
                .aggregate(
                        ColorStats::new,
                        ColorStats::aggregate,
                        Materialized.<String, ColorStats, KeyValueStore<Bytes,byte[]>>
                                        as("colorStats")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(colorStatsSerde));


        // Stream of events with broken light barriers
        KStream<String, HbwEvent> lightBarrierBrokenHBW = hbwTypedStream.filterNot((k, v) ->
                v.getData().isI1_light_barrier() && v.getData().isI4_light_barrier());

        // grouped by light barrier and windowed by session
        SessionWindowedKStream<String, HbwEvent> sessionizedHbwEvent = lightBarrierBrokenHBW
                .groupBy((k, v) -> {
                    String sensorKey = "unknown";
                    if (!v.getData().isI4_light_barrier()) {
                        sensorKey = "i4_light_sensor";
                    } else if (!v.getData().isI1_light_barrier()) {
                        sensorKey = "i1_light_sensor";
                    }
                    System.out.println("LightSensor broken: " + sensorKey);
                    return sensorKey;
                }, Grouped.with(Serdes.String(), hbwEventSerdes))
                .windowedBy(SessionWindows.ofInactivityGapAndGrace(Duration.ofSeconds(2),Duration.ofMillis(500)));




        // aggregated by timedifference of each session
        sessionizedHbwEvent.aggregate(
                TimeDifferenceAggregation::new,
                TimeDifferenceAggregation::add,
                TimeDifferenceAggregation::merge,
                Materialized.<String, TimeDifferenceAggregation, SessionStore<Bytes, byte[]>>as("lightSensor")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(timeDifferenceAggregationSerde)
                        .withCachingEnabled()
                ).suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded().shutDownWhenFull()));



        /* JOINING VGR AND HBW STREAMS */

        // Specify key by the timestamp of the event (rounded to the nearest 10 seconds)
        KStream<String, VgrEvent> vgrTimestampKey = vgrTypedStream.map((key, value) -> {
                long epochSecond = value.getData().getTimestamp().getEpochSecond();
                long keySecond = epochSecond - epochSecond % 10;
                return new KeyValue<>(Long.toString(keySecond), value);
        });

        // Specify key by the timestamp of the event (rounded to the nearest 10 seconds)
        KStream<String, HbwEvent> hbwTimestampKey = hbwTypedStream.map((key, value) -> {
            long epochSecond = value.getData().getTimestamp().getEpochSecond();
            long keySecond = epochSecond - epochSecond % 10;
            return new KeyValue<>(Long.toString(keySecond), value);
        });

        // Specify how to join the two streams in terms of serde
        StreamJoined<String, VgrEvent, HbwEvent> joinParams =
                StreamJoined.with(
                        Serdes.String(),
                        new VgrEventSerdes(),
                        new HbwEventSerdes()
                );

        // Define the temporal boundaries within which records from the two streams will be considered for joining
        JoinWindows joinWindows =
                JoinWindows.ofTimeDifferenceAndGrace(
                        Duration.ofSeconds(5),
                        Duration.ofSeconds(1)
                );

        // Join the two streams
        KStream<String, FactoryStats> factoryStatsStream =
                vgrTimestampKey.join(
                        hbwTimestampKey,
                        FactoryStats::new,
                        joinWindows,
                        joinParams
                );

        // Create a KTable that stores the latest FactoryStats for each key
        factoryStatsStream
                .groupByKey()
                .reduce((aggValue, newValue) -> newValue,
                        Materialized.<String, FactoryStats, KeyValueStore<Bytes, byte[]>>
                                as("factoryStats")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(factoryStatsSerde));

        return builder.build();
    }
}
