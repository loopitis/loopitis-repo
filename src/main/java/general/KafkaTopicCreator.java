package general;

import com.example.demo.ConfigurationManager;
import com.example.demo.LoopitisApplication;
import com.example.demo.LoopitisMainEndpoints;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Properties;

public class KafkaTopicCreator {
    private static final Logger log = LoggerFactory.getLogger(LoopitisApplication.MY_LOGGER);

    public static void createTopicIfNotExists(String topicName, int numPartitions, short replicationFactor, Properties props) {

        try (AdminClient adminClient = AdminClient.create(props)) {
            if (!adminClient.listTopics().names().get().contains(topicName)) {
                NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);
                adminClient.createTopics(Collections.singleton(newTopic)).all().get();
                System.out.println("Topic " + topicName + " created successfully.");
            }
            else {
                System.out.println("Topic " + topicName + " already exists.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

