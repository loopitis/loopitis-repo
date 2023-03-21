package com.example.demo;

import com.google.gson.Gson;
import general.GetNotifierCheckResult;
import general.GetNotifierRequestChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pojos.HttpNotifierRequest;
import producer.KafkaProducer;

@RequestMapping("/set")
@RestController
public class TestEndpoint {
    private static final Logger log = LoggerFactory.getLogger(TestEndpoint.class);
    public static final String TOPIC_NAME = "notifier";
    private static Gson gson = new Gson();

    @RequestMapping("/getNotifier")
    @PostMapping
    public ResponseEntity<String> createGetNotifier(@RequestBody HttpNotifierRequest notif){
        log.debug("I am here");

        GetNotifierCheckResult result = GetNotifierRequestChecker.check(notif);
        if(result != null && result.isError()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.toJson());
        }
        KafkaProducer.getInstance().send(TOPIC_NAME, "get", gson.toJson(notif));

        return ResponseEntity.ok("{ \"message\":\"ok\"}");
    }

    @RequestMapping("/postNotifier")
    @PostMapping
    public ResponseEntity<String> createPostNotifier(@RequestBody HttpNotifierRequest notif){
        return ResponseEntity.ok("ok");
    }
}
