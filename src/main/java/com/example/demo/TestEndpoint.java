package com.example.demo;

import consumer.ConsumerTask;
import general.GetHttpNotifierRequest;
import general.GetNotifierCheckResult;
import general.GetNotifierRquestChecker;
import general.PostHttpNotifierRequest;
import managers.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pojos.HttpNotifierRequest;

@RequestMapping("/set")
@RestController
public class TestEndpoint {
    private static final Logger log = LoggerFactory.getLogger(TestEndpoint.class);

    @RequestMapping("/getNotifier")
    @PostMapping
    public ResponseEntity<String> createGetNotifier(@RequestBody HttpNotifierRequest notif){
        log.debug("I am here");
        var getRequest = new GetHttpNotifierRequest(notif);
        GetNotifierCheckResult result = GetNotifierRquestChecker.check(getRequest);
        if(result != null && result.isError()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.toJson());
        }
        ConsumerTask task = new ConsumerTask(getRequest);
        task.handle();
        return ResponseEntity.ok("{ \"message\":\"ok\"}");
    }

    @RequestMapping("/postNotifier")
    @PostMapping
    public ResponseEntity<String> createPostNotifier(@RequestBody HttpNotifierRequest notif){
        return ResponseEntity.ok("ok");
    }
}
