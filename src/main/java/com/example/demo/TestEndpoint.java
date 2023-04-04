package com.example.demo;

import com.google.gson.Gson;
import ent.HttpNotifierRequestEntity;
import general.GetNotifierCheckResult;
import general.GetNotifierRequestChecker;
import managers.DBhibernetManager;
import managers.RequestManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pojos.HttpNotifierRequest;
import producer.KafkaProducer;

import java.util.UUID;

@RequestMapping("/set")
@RestController
public class TestEndpoint {
    private static final Logger log = LoggerFactory.getLogger(TestEndpoint.class);
    public static final String TOPIC_NAME = ConfigurationManager.getInstance().getKafkaTopic();
    private static Gson gson = new Gson();

    @RequestMapping("/getNotifier")
    @PostMapping
    public ResponseEntity<String> createGetNotifier(@RequestBody HttpNotifierRequest notif){
        log.debug("I am here");

        GetNotifierCheckResult result = GetNotifierRequestChecker.check(notif);
        if(result != null && result.isError()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.toJson());
        }

        UUID uuid = UUID.randomUUID();
        String external_id = uuid.toString();

        notif.setExternal_id(external_id);
        var notifierEntity = new HttpNotifierRequestEntity(notif);
        notifierEntity.setStatus("PENDING");
        notifierEntity.setDone(0);
        Long internalId = RequestManager.getInstance().saveRequest(notifierEntity);//request a call , internal call id, external call id , call name, call status, type(get\call), interval, occurance,  data:json
        notif.setInternal_id(internalId);

        log.info("$$$$$$$$$$$$$$$$$$$$$$$ About to send data to kafka for topic "+TOPIC_NAME);
        KafkaProducer.getInstance().send(TOPIC_NAME, "get", gson.toJson(notif));


        return ResponseEntity.ok("{ \"id\":\""+external_id+"\",\"internal_id\":"+internalId+"}");

    }

    @RequestMapping("/postNotifier")
    @PostMapping
    public ResponseEntity<String> createPostNotifier(@RequestBody HttpNotifierRequest notif){
        return ResponseEntity.ok("ok");
    }
}
