package consumer;

import com.example.demo.ConfigurationManager;
import com.example.demo.TestEndpoint;
import com.google.gson.Gson;
import general.GetHttpNotifierRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojos.HttpNotifierRequest;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;


public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class.getSimpleName());

    private final Properties properties;
    private org.apache.kafka.clients.consumer.KafkaConsumer<String, String> kafkaConsumer;
    private Gson gson = new Gson();

    private final static String KAFKA_HOST = ConfigurationManager.getInstance().getKafkaHost();


    public static void main(String[] args) {

        KafkaConsumer consumer = new KafkaConsumer();
        consumer.run();
    }

    public KafkaConsumer(){

        properties = new Properties();
        String boostrapServers = KAFKA_HOST+":9092";
//        String boostrapServers = "cluster.playground.cdkt.io:9092";
        log.debug("@@@@ bout to connect to Kafka with host "+boostrapServers);

        properties.setProperty("bootstrap.servers", boostrapServers);
//        properties.setProperty("security.protocol","SASL_SSL");
//        properties.setProperty("sasl.mechanism","PLAIN");
//        properties.setProperty("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username='3FCWzHXahINkqWBzARKErZ' password='eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2F1dGguY29uZHVrdG9yLmlvIiwic291cmNlQXBwbGljYXRpb24iOiJhZG1pbiIsInVzZXJNYWlsIjpudWxsLCJwYXlsb2FkIjp7InZhbGlkRm9yVXNlcm5hbWUiOiIzRkNXekhYYWhJTmtxV0J6QVJLRXJaIiwib3JnYW5pemF0aW9uSWQiOjcwNTgzLCJ1c2VySWQiOjgxNjg1LCJmb3JFeHBpcmF0aW9uQ2hlY2siOiIxYTFlMTIyOC1iZGFkLTQ3NDktOWNlMi03MjdiYzQ2N2IyNzkifX0.QqMmHKIAFoiUUF536vYIvOIQfkrUUJyl-HtsN4i4i-s';");

        properties.put("group.id", "my-group");



        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        kafkaConsumer = new org.apache.kafka.clients.consumer.KafkaConsumer<String, String>(properties);
    }


    
    public boolean shutdown(){
        kafkaConsumer.commitSync();
        kafkaConsumer.close();
        return true;
    }


    public void run(){
//        kafkaConsumer.subscribe(Collections.singletonList(TestEndpoint.REQUEST_TASKS_TOPIC));
        kafkaConsumer.subscribe(Collections.singletonList(TestEndpoint.REQUEST_TASKS_TOPIC));
        while (true) {
            log.debug("About to poll from kafka");
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(2000));
            if(records.isEmpty()){
                log.debug("nothing found");

            }
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                if(record.value() == null) continue;
                if(record.topic().equals(TestEndpoint.REQUEST_TASKS_TOPIC)) {
                    HttpNotifierRequest request = gson.fromJson(record.value(), HttpNotifierRequest.class);
                    if (record.key().equals("get")) {
                        var getRequest = new GetHttpNotifierRequest(request);
                        NotifierTask task = new NotifierTask(getRequest);
                        task.handle();
                    }
                }
            }
            kafkaConsumer.commitSync();
        }
    }
}
