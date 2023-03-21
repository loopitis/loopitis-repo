package producer;

import general.KafkaTopicCreator;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.Future;

public class KafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class.getSimpleName());

    private static KafkaProducer instance;
    private final Properties properties;
    private org.apache.kafka.clients.producer.KafkaProducer<String, String> kafkaProducer;

    private KafkaProducer(){
        properties = new Properties();

        String boostrapServers = "cluster.playground.cdkt.io:9092";

        properties.setProperty("bootstrap.servers", boostrapServers);
        properties.setProperty("security.protocol","SASL_SSL");
        properties.setProperty("sasl.mechanism","PLAIN");
        properties.setProperty("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username='3FCWzHXahINkqWBzARKErZ' password='eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2F1dGguY29uZHVrdG9yLmlvIiwic291cmNlQXBwbGljYXRpb24iOiJhZG1pbiIsInVzZXJNYWlsIjpudWxsLCJwYXlsb2FkIjp7InZhbGlkRm9yVXNlcm5hbWUiOiIzRkNXekhYYWhJTmtxV0J6QVJLRXJaIiwib3JnYW5pemF0aW9uSWQiOjcwNTgzLCJ1c2VySWQiOjgxNjg1LCJmb3JFeHBpcmF0aW9uQ2hlY2siOiIxYTFlMTIyOC1iZGFkLTQ3NDktOWNlMi03MjdiYzQ2N2IyNzkifX0.QqMmHKIAFoiUUF536vYIvOIQfkrUUJyl-HtsN4i4i-s';");


        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        kafkaProducer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(properties);
    }

    public synchronized static KafkaProducer getInstance(){
        if(instance == null){
            instance = new KafkaProducer();
        }
        return instance;
    }

    public boolean shutdown(){
        kafkaProducer.flush();
        kafkaProducer.close();
        return true;
    }


    public Future<RecordMetadata> send(String topic, String key, String value){
        var producerRecord = new ProducerRecord<String, String>(topic, key, value);
        return kafkaProducer.send(producerRecord);
    }


}
