package ch.unisg.monitoring.kafka.topology;

import ch.unisg.monitoring.domain.stations.HBW_1;
import ch.unisg.monitoring.domain.stations.VGR_1;

import ch.unisg.monitoring.kafka.serialization.FactoryEvent;
import ch.unisg.monitoring.kafka.serialization.HbwEvent;
import ch.unisg.monitoring.kafka.serialization.VgrEvent;
import ch.unisg.monitoring.kafka.serialization.json.FactoryEventSerdes;
import ch.unisg.monitoring.kafka.serialization.json.hbw.HbwDeserializer;
import ch.unisg.monitoring.kafka.serialization.json.vgr.VgrDeserializer;
import ch.unisg.monitoring.kafka.topology.aggregations.ColorStats;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;

import static ch.unisg.monitoring.kafka.serialization.json.json.JsonSerdes.jsonSerde;


public class ProcessingTopology {
    private static final Gson gsonHBW = new GsonBuilder()
            .registerTypeAdapter(HBW_1 .class, new HbwDeserializer())
            .create();
    private static final Gson gsonVGR = new GsonBuilder()
            .registerTypeAdapter(VGR_1.class, new VgrDeserializer())
            .create();
    public static Topology build() {

        StreamsBuilder builder = new StreamsBuilder();
        Serde<ColorStats> colorStatsSerde = jsonSerde(ColorStats.class);

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
                        Grouped.with(Serdes.String(), new JsonSerde<>(VgrEvent.class)))
                .count()
                .toStream()
                .foreach((key, count) -> System.out.println("Key: " + key + ", Count: " + count));



        return builder.build();
    }
}
