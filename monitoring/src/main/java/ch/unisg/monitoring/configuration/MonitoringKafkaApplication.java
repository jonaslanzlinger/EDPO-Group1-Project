package ch.unisg.monitoring.configuration;

import ch.unisg.monitoring.kafka.topology.aggregations.FactoryStats;
import ch.unisg.monitoring.kafka.topology.ProcessingTopology;
import ch.unisg.monitoring.kafka.topology.aggregations.ColorStats;
import ch.unisg.monitoring.kafka.topology.aggregations.TimeDifferenceAggregation;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.QueryableStoreTypes;

import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.ReadOnlySessionStore;
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
        config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 10 * 1024 * 1024L); // 10MB cache


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
    public ReadOnlySessionStore<String, TimeDifferenceAggregation> lightSensorStore(KafkaStreams streams) {
        return streams.store(StoreQueryParameters.fromNameAndType("lightSensor", QueryableStoreTypes.sessionStore()));
    }

    @Bean
    public ReadOnlyKeyValueStore<String, FactoryStats> factoryStatsStore(KafkaStreams streams) {
        return streams.store(StoreQueryParameters.fromNameAndType("factoryStats", QueryableStoreTypes.keyValueStore()));
    }
}