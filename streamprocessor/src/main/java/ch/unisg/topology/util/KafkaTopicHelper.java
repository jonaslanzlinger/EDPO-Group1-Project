package ch.unisg.topology.util;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Collections;
import java.util.Properties;
import java.util.Set;

/**
 * This class is used to create a Kafka topic if it does not exist.
 */
public class KafkaTopicHelper {

    public static void createTopicIfNotExists(Properties config, String topicName, int numPartitions, short replicationFactor) {

        try (AdminClient adminClient = AdminClient.create(config)) {
            ListTopicsResult listTopicsResult = adminClient.listTopics();
            Set<String> topics = listTopicsResult.names().get();

            if (!topics.contains(topicName)) {
                NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);
                adminClient.createTopics(Collections.singleton(newTopic));
                System.out.println("Created topic " + topicName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
