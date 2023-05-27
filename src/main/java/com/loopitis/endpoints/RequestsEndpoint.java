package com.loopitis.endpoints;

import com.google.gson.Gson;
import com.loopitis.enums.eRequestStatus;
import com.loopitis.filters.RequestsFilter;
import com.loopitis.general.CancelTaskRequest;
import com.loopitis.general.ErrorDetails;
import com.loopitis.general.ShowRequestsRequest;
import com.loopitis.managers.RedisManager;
import com.loopitis.managers.RequestsManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.loopitis.endpoints.LoopitisMainEndpoints.REDIS_CANCEL_CHANNEL;

@RequestMapping("/requests")
@RestController
public class RequestsEndpoint {

    public static final String CANCEL_PATH = "requests/cancel_task";
    public static final String REQUEST_LIST_EP = "requests/get_list";
    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(LoopitisApplication.MY_LOGGER);

    private Gson g = new Gson();

    public static String generateShowRequest(String externalId) {
        return ConfigurationManager.getInstance().getEndpointHost() + "/" + RequestsEndpoint.REQUEST_LIST_EP + "?requestId=" + externalId;
    }

    public static String generateCancelLink(String externalId) {
        return ConfigurationManager.getInstance().getEndpointHost() + "/" + RequestsEndpoint.CANCEL_PATH + "?requestId=" + externalId;
    }

    @RequestMapping("/list")
    @PostMapping
    public ResponseEntity<String> showRequests(@RequestBody ShowRequestsRequest req) {
        log.debug("Received getExecution with data  " + g.toJson(req));

        if (req == null) {
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
    public ResponseEntity<String> getRequests(@RequestParam(value = "requestId", required = false) String requestId, @RequestParam(value = "status", required = false) eRequestStatus status) {
        log.debug("Received get request  ");

        if (requestId == null && status == null) {
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
    public ResponseEntity<String> cancelTaskByGet(@RequestParam("requestId") String requestId) {
        log.debug("Received Request to cancel request " + requestId);
        if (requestId == null || requestId.isEmpty()) {
            ErrorDetails details = new ErrorDetails();
            details.withCode(400)
                    .withField("requestId")
                    .withDetails("requestId not provided")
                    .withMessage("Bad input parameters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(g.toJson(details));

        }
        CancelTaskRequest request = new CancelTaskRequest(requestId);
        //save the url to redis
        RedisManager.getInstance().publishMessageToChannel(REDIS_CANCEL_CHANNEL, g.toJson(request));
        String showRequestLink = generateShowRequest(requestId);
        return ResponseEntity.ok().body("{\"result\":\"request sent\", \"show-request\":" + showRequestLink + "}");

    }

}
