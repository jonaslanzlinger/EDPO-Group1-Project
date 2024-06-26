package ch.unisg.monitoring.configuration;

import ch.unisg.monitoring.kafka.serialization.timestampExtractors.CustomTimestampExtractor;
import ch.unisg.monitoring.kafka.topology.aggregations.FactoryStats;
import ch.unisg.monitoring.kafka.topology.ProcessingTopology;
import ch.unisg.monitoring.kafka.topology.aggregations.ColorStats;
import ch.unisg.monitoring.kafka.topology.aggregations.TimeDifferenceAggregation;
import ch.unisg.monitoring.utils.KafkaTopicHelper;
import org.apache.kafka.common.serialization.Serdes;
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

/**
 * This class is responsible for configuring the Kafka Streams application.
 */
@Configuration
public class MonitoringKafkaApplication {

    @Bean
    public KafkaStreams kafkaStreams() {
        Topology topology = ProcessingTopology.build();

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "monitoring");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        config.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, CustomTimestampExtractor.class.getName());
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        String[] topics = {"VGR_1", "HBW_1", "factory-all", "monitoring-all"};

        for (String station : topics) {
            KafkaTopicHelper.createTopicIfNotExists(config, station, 1, (short) 1);
        }

        KafkaStreams streams = new KafkaStreams(topology, config);

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        streams.start();

        return streams;
    }

    /**
     * The following beans are used to query the state stores from the REST API.
     */

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