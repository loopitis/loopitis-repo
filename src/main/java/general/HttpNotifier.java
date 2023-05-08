package general;

import com.example.demo.ConfigurationManager;
import com.example.demo.LoopitisApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import consumer.ExecutionRequest;
import enums.eCallbackType;
import enums.eEvent;
import managers.DBManager;
import managers.EventManager;
import pojos.HttpNotifierRequestTranslated;
import services.RESTServices;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Calendar;

import static com.example.demo.RequestsEndpoint.generateCancelLink;
import static com.example.demo.RequestsEndpoint.generateShowRequest;


public class HttpNotifier {
    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(LoopitisApplication.MY_LOGGER);

    public static final int httpVersion = ConfigurationManager.getInstance().getHttpVersion();//notifications_http_version=2
    public static HttpClient.Version HTTP_VERSION = HttpClient.Version.HTTP_1_1;
    static{
        HTTP_VERSION = httpVersion == 2 ? HttpClient.Version.HTTP_2 : HttpClient.Version.HTTP_1_1;
    }

    private static final Gson g = new GsonBuilder()
            .disableHtmlEscaping()
            .create();

    private HttpNotifierRequestTranslated notifierRequest;

    public HttpNotifier(pojos.HttpNotifierRequestTranslated notif){
        this.notifierRequest = notif;
    }


    public String getRequestId() {
        if(notifierRequest != null) return notifierRequest.getExternal_id();
        return null;
    }


    public boolean fire(String executionId, int executionNumber) {
        try {
            log.debug("About to fire a "+notifierRequest.getCallback_type()+" request ");

            HttpRequest request = getHttpRequest(executionId, executionNumber);


            HttpClient client = HttpClient.newBuilder()
                    .version(HTTP_VERSION)
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();


            //pre execution - saving the execution in the execution list without status code (which will be received right after the execution)
            ExecutionRequest exec = new ExecutionRequest(executionId, notifierRequest.getExternal_id(), Calendar.getInstance().getTimeInMillis());
            DBManager.getInstance().savePreExecution(exec);

            //actual execution
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response == null ? -1 : response.statusCode();
            exec.setStatusCode(statusCode);

            //post-execution - saving status code and updating request with how many done
            DBManager.getInstance().savePostExecution(exec);
            DBManager.getInstance().countExecutions(notifierRequest.getExternal_id());

            //get all client listeners and let them know
            EventManager.getInstance().fire(eEvent.EXECUTION_FIRED, g.toJson(exec));

            log.info("Execution finished with status "+statusCode);
            if(response != null) {
                String responseBody = response.body();
                if(responseBody.length() > 1) {
                    int length = Math.min(response.body().length() - 1, 100);
                    log.info("response (first 100 chars) from client: " + response.body().substring(0, length) + "...");
                }
                else{
                    log.info("Got empty response from client ");
                }
            }
            System.out.println("status code is "+statusCode);
            //notify if requested for status not ok
            if(statusCode != 200 && notifierRequest.getNotify_status_not_ok() != null){
                System.out.println("status code is not ok and user provided a notification endpoint "+notifierRequest.getNotify_status_not_ok());
                //status code is not ok and user provided a notification endpoint
                log.debug("status code is not ok and user provided a notification endpoint "+notifierRequest.getNotify_status_not_ok());
                RESTServices.POST(notifierRequest.getNotify_status_not_ok(), g.toJson(exec));

            }

        }
        catch(Exception ex){
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    private HttpRequest getHttpRequest(String executionId, int executionNumber) {
        if(notifierRequest.getCallback_type() == eCallbackType.GET){
            log.debug("Building a GET request for "+executionId);
            return generateGEThttpRequest(executionId, executionNumber);
        }
        else if(notifierRequest.getCallback_type() == eCallbackType.POST){
            log.debug("Building a POST request for "+executionId);
            return generatePOSThttpRequest(executionId, executionNumber);
        }
        return null;

    }

    private HttpRequest generatePOSThttpRequest(String executionId, int executionNumber) {

        var jsonBody = getContent(executionId, executionNumber, eCallbackType.POST);
        System.out.println("json string response is "+jsonBody);
        URI url = URI.create(notifierRequest.getReturn_url());

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .uri(url)
                .timeout(Duration.ofSeconds(3))
                .header("Content-Type", "application/json")
                .build();

        return request;
    }

    private String getContent(String executionId, int executionNumber, eCallbackType type) {
        String cancellationLink = generateCancelLink(notifierRequest.getExternal_id());
        String showRequest = generateShowRequest(notifierRequest.getExternal_id());
        if(type == eCallbackType.POST) {
            var res = new ExecutionResponse(
                    executionId,
                    executionNumber,
                    notifierRequest.getPayload(),
                    notifierRequest.getOccurrences(),
                    notifierRequest.getExternal_id(),
                    notifierRequest.getName(),
                    cancellationLink,
                    showRequest);
            return g.toJson(res);
        }


        if(type == eCallbackType.GET) {

            StringBuilder builder = new StringBuilder();
            builder.append("?executionId=").append(URLEncoder.encode(executionId, Charset.forName("UTF-8")));
            builder.append("&executionNumber=").append(executionNumber);
            builder.append("&parentRequestId=").append(URLEncoder.encode(notifierRequest.getExternal_id(), Charset.forName("UTF-8")));
            builder.append("&cancelink=").append(URLEncoder.encode(cancellationLink, Charset.forName("UTF-8")));
            builder.append("&showRequest=").append(URLEncoder.encode(showRequest, Charset.forName("UTF-8")));
            if(notifierRequest.getName() != null){
                builder.append("&parentRequestName=").append(URLEncoder.encode(notifierRequest.getName(), Charset.forName("UTF-8")));
            }
            if (notifierRequest.getPayload() != null) {
                builder.append("&payload=").append(URLEncoder.encode(notifierRequest.getPayload(), Charset.forName("UTF-8")));
            }
            if(notifierRequest.getOccurrences() > 0){
                builder.append("&occurrences=").append(notifierRequest.getOccurrences());
            }

            return builder.toString();
        }
        log.debug("Unknown callback type ! "+type);
        return null;
    }



    private HttpRequest generateGEThttpRequest(String executionId, int executionNumber) {
        StringBuilder uriBuilder = new StringBuilder(notifierRequest.getReturn_url());
        uriBuilder.append(getContent(executionId, executionNumber, eCallbackType.GET));


        URI url = URI.create(uriBuilder.toString());

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .timeout(Duration.ofSeconds(3))
                .build();
        return request;
    }


    public Long getDelay() {
       return notifierRequest.getDelay();
    }


    public Long getInterval() {
       return notifierRequest.getInterval();
    }

    public Integer getOccurrence() {
        return notifierRequest.getOccurrences();
    }
}
