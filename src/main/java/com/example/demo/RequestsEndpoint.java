package com.example.demo;

import com.google.gson.Gson;
import filters.ExecutionsFilter;
import filters.RequestsFilter;
import general.ShowExecutionsRequest;
import general.ShowRequestsRequest;
import managers.ExecutionsManager;
import managers.RequestsManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/requests")
@RestController
public class RequestsEndpoint {

    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(RequestsEndpoint.class);

    private Gson g = new Gson();

    @RequestMapping("/list")
    @PostMapping
    public ResponseEntity<String> showRequests(@RequestBody ShowRequestsRequest req){
        log.debug("Received getExecution with data  "+g.toJson(req));

        if(req == null || req.getStatus() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        RequestsFilter filter = new RequestsFilter();
        filter
                .withStatus(req.getStatus())
                .withLimit(50);

        var res = RequestsManager.getInstance().getRequests(filter);


        return ResponseEntity.ok(g.toJson(res));

    }

}
