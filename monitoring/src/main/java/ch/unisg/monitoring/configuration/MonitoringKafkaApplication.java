package ch.unisg.monitoring.configuration;

import ch.unisg.monitoring.kafka.topology.FactoryStats;
import ch.unisg.monitoring.kafka.topology.ProcessingTopology;
import ch.unisg.monitoring.kafka.topology.aggregations.ColorStats;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.QueryableStoreTypes;

import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class MonitoringKafkaApplication {

    @Bean
    public KafkaStreams kafkaStreams() {
        Topology topology = ProcessingTopology.build();

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "monitoring");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");

        KafkaStreams streams = new KafkaStreams(topology, config);

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        System.out.println("Starting streams...");
        streams.start();

        return streams;
    }

    @Bean
    public ReadOnlyKeyValueStore<String, ColorStats> colorStatsStore(KafkaStreams streams) {
        return streams.store(StoreQueryParameters.fromNameAndType("colorStats", QueryableStoreTypes.keyValueStore()));
    }

    @Bean
    public ReadOnlyKeyValueStore<String, FactoryStats> factoryStatsStore(KafkaStreams streams) {
        return streams.store(StoreQueryParameters.fromNameAndType("factoryStats", QueryableStoreTypes.keyValueStore()));
    }
}