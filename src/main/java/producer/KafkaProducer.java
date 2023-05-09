package producer;

import com.example.demo.ConfigurationManager;
import com.example.demo.LoopitisApplication;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.Future;

public class KafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(LoopitisApplication.MY_LOGGER);
    private final static String KAFKA_HOST = ConfigurationManager.getInstance().getKafkaHost();
    private static KafkaProducer instance;
    private final Properties properties;
    private org.apache.kafka.clients.producer.KafkaProducer<String, String> kafkaProducer;

    private KafkaProducer() {
        properties = new Properties();
        String boostrapServers = KAFKA_HOST + ":9092";

        properties.setProperty("bootstrap.servers", boostrapServers);
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        kafkaProducer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(properties);
    }

    public synchronized static KafkaProducer getInstance() {
        if (instance == null) {
            instance = new KafkaProducer();
        }
        return instance;
    }

    public boolean shutdown() {
        kafkaProducer.flush();
        kafkaProducer.close();
        return true;
    }


    public Future<RecordMetadata> send(String topic, String value) {
        log.debug("Sending data to " + properties.get("bootstrap.servers"));
        var producerRecord = new ProducerRecord<String, String>(topic, value);
        return kafkaProducer.send(producerRecord);
    }


}
