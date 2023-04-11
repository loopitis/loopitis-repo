package general;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

public class KafkaTopicCreator {
    public static void main(String[] args) {
        var properties = new Properties();

        String boostrapServers = "localhost:9092";

        properties.setProperty("bootstrap.servers", boostrapServers);
//        properties.setProperty("security.protocol","SASL_SSL");
//        properties.setProperty("sasl.mechanism","PLAIN");
//        properties.setProperty("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username='3FCWzHXahINkqWBzARKErZ' password='eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2F1dGguY29uZHVrdG9yLmlvIiwic291cmNlQXBwbGljYXRpb24iOiJhZG1pbiIsInVzZXJNYWlsIjpudWxsLCJwYXlsb2FkIjp7InZhbGlkRm9yVXNlcm5hbWUiOiIzRkNXekhYYWhJTmtxV0J6QVJLRXJaIiwib3JnYW5pemF0aW9uSWQiOjcwNTgzLCJ1c2VySWQiOjgxNjg1LCJmb3JFeHBpcmF0aW9uQ2hlY2siOiIxYTFlMTIyOC1iZGFkLTQ3NDktOWNlMi03MjdiYzQ2N2IyNzkifX0.QqMmHKIAFoiUUF536vYIvOIQfkrUUJyl-HtsN4i4i-s';");

        properties.put("group.id", "my-group");



        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        createTopicIfNotExists("my_topic", 3, (short)1, properties);
    }
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

