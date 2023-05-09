package consumer;

import com.example.demo.ConfigurationManager;
import com.example.demo.LoopitisApplication;
import com.example.demo.LoopitisMainEndpoints;
import com.google.gson.Gson;
import enums.eRequestStatus;
import general.CancelTaskRequest;
import general.FutureCancel;
import general.HttpNotifier;
import managers.DBManager;
import managers.RedisManager;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojos.HttpNotifierRequestTranslated;
import redis.clients.jedis.JedisPubSub;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;


public class LoopitisConsumer {

    private static final Logger log = LoggerFactory.getLogger(LoopitisApplication.MY_LOGGER);
    private final static String KAFKA_HOST = ConfigurationManager.getInstance().getKafkaHost();
    private final Properties properties;
    private org.apache.kafka.clients.consumer.KafkaConsumer<String, String> kafkaConsumer;
    private Gson gson = new Gson();
    private HashMap<String, FutureCancel> taskIdToFuture = new HashMap<>();

    private Gson g = new Gson();

    public LoopitisConsumer() {

        properties = new Properties();
        String boostrapServers = KAFKA_HOST + ":9092";
        log.debug("@@@@ bout to connect to Kafka with host " + boostrapServers);

        properties.setProperty("bootstrap.servers", boostrapServers);
        properties.put("group.id", "my-group");


        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        kafkaConsumer = new org.apache.kafka.clients.consumer.KafkaConsumer<String, String>(properties);
    }

    public static void main(String[] args) {
        log.debug("Starting Kafka Consumer");
        System.out.println("Kafka consumer");
        LoopitisConsumer consumer = new LoopitisConsumer();
        consumer.run();
    }

    public boolean shutdown() {
        kafkaConsumer.commitSync();
        kafkaConsumer.close();
        return true;
    }


    public void run() {
        //subscribe to channel so it could cancel requests
        Thread channelSubscriber = getThreadSubscriber();
        channelSubscriber.start();

        //subscribe to topic in kafka to get more requests
        kafkaConsumer.subscribe(Collections.singletonList(LoopitisMainEndpoints.REQUEST_TASKS_TOPIC));


        while (true) {
            log.debug("About to poll from kafka");
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(2000));
            if (records.isEmpty()) {
                log.debug("nothing found");
                continue;
            }
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                if (record.value() == null) continue;
                if (record.topic().equals(LoopitisMainEndpoints.REQUEST_TASKS_TOPIC)) {
                    pojos.HttpNotifierRequestTranslated request = gson.fromJson(record.value(), HttpNotifierRequestTranslated.class);
                    var getRequest = new HttpNotifier(request);
                    NotifierTask task = new NotifierTask(getRequest);

                    log.debug("Updating DB task ongoing " + request.getExternal_id());
                    //update DB that the task is in progress
                    DBManager.getInstance().updateStatus(request.getExternal_id(), eRequestStatus.ON_GOING);

                    var future = task.handle();
                    taskIdToFuture.put(request.getExternal_id(), future);

                }
            }
            kafkaConsumer.commitSync();
        }
    }

    private Thread getThreadSubscriber() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                RedisManager.getInstance().subscribeToChannel(LoopitisMainEndpoints.REDIS_CANCEL_CHANNEL, new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        cancelRequest(channel, message);
                    }
                });
            }
        });
    }

    public void cancelRequest(String channel, String message) {
        System.out.println("$$$$$$$$$$$$$$$$$$$$ Msg from channel: " + channel + " Message: " + message + "$$$$$$$$$$$$$$$$$$$$$$$");
        if (channel.equals(LoopitisMainEndpoints.REDIS_CANCEL_CHANNEL)) {
            CancelTaskRequest cancelTaskRequest = g.fromJson(message, CancelTaskRequest.class);
            FutureCancel future = taskIdToFuture.get(cancelTaskRequest.getRequestId());
            if (future == null) {
                log.debug("This consumer is not fmailiar with task " + cancelTaskRequest.getRequestId());
                log.debug("Ignoring message");
                return;
            }
            log.debug("This consumer is found the requested task " + cancelTaskRequest.getRequestId());
            log.debug("Canceling the task...");
            boolean canceledResult = future.cancel();
            if (!canceledResult) {
                log.debug("FAILED cancelling task " + cancelTaskRequest.getRequestId());
                return;
            }
            log.debug("Task canceled successfully " + cancelTaskRequest.getRequestId());
            taskIdToFuture.remove(cancelTaskRequest.getRequestId());
            DBManager.getInstance().updateStatus(cancelTaskRequest.getRequestId(), eRequestStatus.CANCELED);
        } else {
            log.error("This consumer is not fmailiar with the request type " + message);
            log.error("Ignoring message");
        }

    }
}
