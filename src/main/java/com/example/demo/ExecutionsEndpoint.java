package com.example.demo;

import com.google.gson.Gson;
import ent.HttpNotifierRequestEntity;
import enums.eEvent;
import filters.ExecutionsFilter;
import general.GetNotifierCheckResult;
import general.GetNotifierRequestChecker;
import general.ShowExecutionsRequest;
import managers.EventManager;
import managers.ExecutionsManager;
import managers.RequestManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pojos.HttpNotifierRequest;
import producer.KafkaProducer;

import java.util.UUID;

@RequestMapping("/executions")
@RestController
public class ExecutionsEndpoint {
    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(ExecutionsEndpoint.class);

    private Gson g = new Gson();

    @RequestMapping("/list")
    @PostMapping
    public ResponseEntity<String> getExecutions(@RequestBody ShowExecutionsRequest req){
        log.debug("Received getExecution with data  "+g.toJson(req));
        //to delete later
        ConfigurationManager.getInstance();

        if(req == null || req.getRequestId() == null || req.getRequestId().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        ExecutionsFilter filter = new ExecutionsFilter();
        filter
                .withRequestId(req.getRequestId())
                .withLimit(50);

        var res = ExecutionsManager.getInstance().getExecutions(filter);


        return ResponseEntity.ok(g.toJson(res));

    }
}
