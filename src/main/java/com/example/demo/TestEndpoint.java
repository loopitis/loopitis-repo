package com.example.demo;

import com.google.gson.Gson;
import ent.HttpNotifierRequestEntity;
import enums.eEvent;
import general.*;
import managers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pojos.HttpNotifierRequest;
import producer.KafkaProducer;
import services.Services;

import java.util.Arrays;
import java.util.UUID;

@RequestMapping("/set")
@RestController
public class TestEndpoint {
    private static final Logger log = LoggerFactory.getLogger(DemoApplication.MY_LOGGER);
    public static final String REQUEST_TASKS_TOPIC = ConfigurationManager.getInstance().getKafkaTopic();
    public static final String REDIS_CANCEL_CHANNEL = ConfigurationManager.getInstance().getRedisCancelChannel();
    private static Gson gson = new Gson();

    @RequestMapping("/getNotifier")
    @PostMapping
    public ResponseEntity<String> createGetNotifier(@RequestBody HttpNotifierRequest notif){
        log.debug("I am here 222222");

        NotifierCheckResult result = NotifierRequestChecker.check(notif);
        if(result != null && result.isError()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(result));
        }
        NotifierRequestChecker.setDefaultsForMissingValues(notif);
        UUID uuid = UUID.randomUUID();
        String external_id = uuid.toString();

        notif.setExternal_id(external_id);
        var notifierEntity = new HttpNotifierRequestEntity(notif);
        notifierEntity.setStatus("PENDING");
        notifierEntity.setDone(0);
        Long internalId = RequestManager.getInstance().saveRequest(notifierEntity);//request a call , internal call id, external call id , call name, call status, type(get\call), interval, occurance,  data:json
        notif.setInternal_id(internalId);

        log.info("$$$$$$$$$$$$$$$$$$$$$$$ About to send data to kafka for topic "+ REQUEST_TASKS_TOPIC);
        var future = KafkaProducer.getInstance().send(REQUEST_TASKS_TOPIC, gson.toJson(notif));
        if(future != null){
            log.debug("Request task Added successfully to Kafka ");
        }
        EventManager.getInstance().fire(eEvent.REQUEST_ADDED, gson.toJson(notif));

        return ResponseEntity.ok("{ \"id\":\""+external_id+"\",\"internal_id\":"+internalId+"}");

    }


    @RequestMapping("/connection")
    @PostMapping
    public ResponseEntity<String> connect(@RequestBody ConnectRequest connectRequest){
        log.debug("Received Request to connect "+connectRequest);
        if(connectRequest == null || connectRequest.getUrl() == null || connectRequest.getUrl().isEmpty()) {
            ErrorDetails details = new ErrorDetails();
            details.withCode(400)
                    .withField("url")
                    .withDetails("url not provided")
                    .withMessage("Bad input parameters");
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(details));

        }
        if(!Services.isValidURL(connectRequest.getUrl())){
            ErrorDetails details = new ErrorDetails();
            details.withCode(400)
                    .withField("url")
                    .withDetails("url not valid")
                    .withMessage("Bad input parameters");
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(details));
        }
        log.debug("Receievd request to "+connectRequest);


        //save the url to redis
        ConnectionNotifierManager.getInstance().saveConnectionRequest(connectRequest, Arrays.stream(eEvent.values()).toList()/*listen on all events*/);
        String response = "{\"connected\": \"true\", \"url\": \""+connectRequest.getUrl()+"\"}";
        return ResponseEntity.ok().body(response);

    }

//    @RequestMapping("/cancel_task")
//    @PostMapping
//    public ResponseEntity<String> cancelTask(@RequestBody CancelTaskRequest cancelRequest){
//        log.debug("Received Request to cancel "+cancelRequest);
//        if(cancelRequest == null || cancelRequest.getRequestId() == null || cancelRequest.getRequestId().isEmpty()) {
//            ErrorDetails details = new ErrorDetails();
//            details.withCode(400)
//                    .withField("requestId")
//                    .withDetails("requestId not provided")
//                    .withMessage("Bad input parameters");
//            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(details));
//
//        }
//
//        //save the url to redis
//        RedisManager.getInstance().publishMessageToChannel(REDIS_CANCEL_CHANNEL, gson.toJson(cancelRequest));
//        return ResponseEntity.ok().body("{\"result\":\"request sent\"}");
//
//    }





    @RequestMapping("/execution/comment")
    @PostMapping
    public ResponseEntity<String> addCommentToSpecificExecution(@RequestBody CommentRequest commentRequest){
        log.debug("Received Request to comment execution "+commentRequest);
        if(commentRequest == null || commentRequest.getExecutionId() == null || commentRequest.getExecutionId().isEmpty()) {
            ErrorDetails details = new ErrorDetails();
            details.withCode(400)
                    .withField("executionId")
                    .withDetails("executionId not provided")
                    .withMessage("Bad input parameters");
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(details));

        }

        if(commentRequest == null || commentRequest.getComment() == null || commentRequest.getComment().isEmpty()) {
            ErrorDetails details = new ErrorDetails();
            details.withCode(400)
                    .withField("comment")
                    .withDetails("comment not provided")
                    .withMessage("Bad input parameters");
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(details));

        }

        //save the url to redis
        boolean added = DBhibernetManager.getInstance().updateCommentOnExecution(commentRequest);
        if(!added){
            ErrorDetails details = new ErrorDetails();
            details.withCode(500)
                    .withField("executionId")
                    .withDetails("Failed adding comment")
                    .withMessage("Execution id was not found");
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(details));

        }
        return ResponseEntity.ok().body("{\"result\":\"comment added\"}");

    }

}
