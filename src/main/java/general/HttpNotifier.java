package general;

import com.example.demo.ConfigurationManager;
import com.google.gson.Gson;
import consumer.ExecutionRequest;
import enums.eCallbackType;
import enums.eEvent;
import managers.DBhibernetManager;
import managers.EventManager;
import pojos.HttpNotifierRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Calendar;


public class HttpNotifier {
    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(HttpNotifier.class);

    public static final int httpVersion = ConfigurationManager.getInstance().getHttpVersion();//notifications_http_version=2
    public static HttpClient.Version HTTP_VERSION = HttpClient.Version.HTTP_1_1;
    static{
        HTTP_VERSION = httpVersion == 2 ? HttpClient.Version.HTTP_2 : HttpClient.Version.HTTP_1_1;
    }

    private static final Gson g = new Gson();

    private HttpNotifierRequest notifierRequest;

    public static void main(String[] args) {
        HttpNotifierRequest req = new HttpNotifierRequest("https://google.com", 5000L, 10000L,"payload123",1L, 20, eCallbackType.POST);
        HttpNotifier n = new HttpNotifier(req);
        n.fire("execid123");
    }


    public HttpNotifier(pojos.HttpNotifierRequest notif){
        this.notifierRequest = notif;
    }



    public String getRequestId() {
        if(notifierRequest != null) return notifierRequest.getExternal_id();
        return null;
    }


    public boolean fire(String executionId) {
        try {
            log.debug("About to fire a "+notifierRequest.getCallback_type()+" request ");

            HttpRequest request = getHttpRequest(executionId);


            HttpClient client = HttpClient.newBuilder()
                    .version(HTTP_VERSION)
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();


            //pre execution - saving the execution in the execution list without status code (which will be received right after the execution)
            ExecutionRequest exec = new ExecutionRequest(executionId, notifierRequest.getExternal_id(), Calendar.getInstance().getTimeInMillis());
            DBhibernetManager.getInstance().savePreExecution(exec);

            //actual execution
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response == null ? -1 : response.statusCode();
            exec.setStatusCode(statusCode);

            //post-execution - saving status code and updating request with how many done
            DBhibernetManager.getInstance().savePostExecution(exec);
            DBhibernetManager.getInstance().countExecutions(notifierRequest.getExternal_id());

            //get all client listeners and let them know
            EventManager.getInstance().fire(eEvent.EXECUTION_FIRED, g.toJson(exec));

            log.info(statusCode);
            if(response != null)
                log.info(response.body());

        }
        catch(Exception ex){
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    private HttpRequest getHttpRequest(String executionId) {
        if(notifierRequest.getCallback_type() == eCallbackType.GET){
            log.debug("Building a GET request for "+executionId);
            return generateGEThttpRequest(executionId);
        }
        else if(notifierRequest.getCallback_type() == eCallbackType.POST){
            log.debug("Building a POST request for "+executionId);
            return generatePOSThttpRequest(executionId);
        }
        return null;

    }

    private HttpRequest generatePOSThttpRequest(String executionId) {

        var requestBody = new ExecutionResponse(executionId, notifierRequest.getPayload());
        String jsonBody = g.toJson(requestBody);
        URI url = URI.create(notifierRequest.getReturn_url());

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .uri(url)
                .timeout(Duration.ofSeconds(3))
                .build();

        return request;
    }

    private HttpRequest generateGEThttpRequest(String executionId) {
        StringBuilder uriBuilder = new StringBuilder(notifierRequest.getReturn_url());
        uriBuilder.append("?execution_id=").append(executionId);
        if (notifierRequest.getPayload() != null) {
            uriBuilder.append("&payload=").append(notifierRequest.getPayload());
        }

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
