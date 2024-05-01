package ch.unisg;


import ch.unisg.topology.ProcessingTopology;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Properties;


@SpringBootApplication
public class StreamProcessorApplication {
    public static void main(String[] args) {
        SpringApplication.run(StreamProcessorApplication.class, args);


        // do we even need this?!!
        enum STATIONS {
            VGR_1, MM_1, HBW_1, EC_1, SM_1, OV_1, WT_1
        }

        for (STATIONS station : STATIONS.values()) {
            TopicBuilder.name(station.toString()).partitions(1).replicas(1).build();
        }




        Topology topology = ProcessingTopology.build();

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "streamprocessor");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");

        KafkaStreams streams = new KafkaStreams(topology, config);

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        System.out.println("Starting streams...");
        streams.start();
    }
}