package com.example.demo;

import com.google.gson.Gson;
import filters.ExecutionsFilter;
import filters.RequestsFilter;
import general.CancelTaskRequest;
import general.ErrorDetails;
import general.ShowExecutionsRequest;
import general.ShowRequestsRequest;
import managers.ExecutionsManager;
import managers.RedisManager;
import managers.RequestsManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import enums.eRequestStatus;

import static com.example.demo.TestEndpoint.REDIS_CANCEL_CHANNEL;

@RequestMapping("/requests")
@RestController
public class RequestsEndpoint {

    public static final String CANCEL_PATH = "requests/cancel_task";
    public static final String REQUEST_LIST_EP = "requests/get_list";
    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(DemoApplication.MY_LOGGER);

    private Gson g = new Gson();

    @RequestMapping("/list")
    @PostMapping
    public ResponseEntity<String> showRequests(@RequestBody ShowRequestsRequest req){
        log.debug("Received getExecution with data  "+g.toJson(req));

        if(req == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        RequestsFilter filter = new RequestsFilter();
        filter
                .withStatus(req.getStatus())
                .withRequestId(req.getRequestId())
                .withLimit(50);

        var res = RequestsManager.getInstance().getRequests(filter);


        return ResponseEntity.ok(g.toJson(res));

    }

    @RequestMapping("/get_list")
    @GetMapping
    public ResponseEntity<String> getRequests(@RequestParam("requestId") String requestId, @RequestParam("status") eRequestStatus status){
        log.debug("Received get request  ");

        if(requestId == null && status == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        RequestsFilter filter = new RequestsFilter();
        filter
                .withStatus(status)
                .withRequestId(requestId)
                .withLimit(50);

        var res = RequestsManager.getInstance().getRequests(filter);


        return ResponseEntity.ok(g.toJson(res));

    }

    @RequestMapping("/cancel_task")
    @GetMapping
    public ResponseEntity<String> cancelTaskByGet(@RequestParam("requestId") String requestId){
        log.debug("Received Request to cancel request "+requestId);
        if(requestId == null || requestId.isEmpty()){
            ErrorDetails details = new ErrorDetails();
            details.withCode(400)
                    .withField("requestId")
                    .withDetails("requestId not provided")
                    .withMessage("Bad input parameters");
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(g.toJson(details));

        }
        CancelTaskRequest request = new CancelTaskRequest(requestId);
        //save the url to redis
        RedisManager.getInstance().publishMessageToChannel(REDIS_CANCEL_CHANNEL, g.toJson(request));
        return ResponseEntity.ok().body("{\"result\":\"request sent\"}");

    }




}
