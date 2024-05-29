package ch.unisg;

import ch.unisg.serialization.timestampExtractors.CustomTimestampExtractor;
import ch.unisg.topology.ProcessingTopology;
import ch.unisg.topology.util.KafkaTopicHelper;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class StreamProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamProcessorApplication.class, args);

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "streamprocessor");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        config.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, CustomTimestampExtractor.class.getName());

        String[] topics = {"VGR_1", "HBW_1", "factory-all", "monitoring-all"};

        for (String station : topics) {
            KafkaTopicHelper.createTopicIfNotExists(config, station, 1, (short) 1);
        }

        Topology topology = ProcessingTopology.build();

        KafkaStreams streams = new KafkaStreams(topology, config);

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        streams.start();
    }
}