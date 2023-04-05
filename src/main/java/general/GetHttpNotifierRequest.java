package general;

import com.example.demo.ConfigurationManager;
import consumer.ExecutionRequest;
import interfaces.I_NotifierRequest;
import managers.DBhibernetManager;
import pojos.HttpNotifierRequest;

import javax.ws.rs.core.UriBuilder;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;


public class GetHttpNotifierRequest implements I_NotifierRequest {
    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(GetHttpNotifierRequest.class);

    public static final int httpVersion = ConfigurationManager.getInstance().getHttpVersion();//notifications_http_version=2
    HttpClient.Version HTTP_VERSION = HttpClient.Version.HTTP_1_1;
    static{
        HttpClient.Version HTTP_VERSION = httpVersion == 2 ? HttpClient.Version.HTTP_2 : HttpClient.Version.HTTP_1_1;
    }
    private HttpNotifierRequest notifierRequest;
    public GetHttpNotifierRequest(HttpNotifierRequest notif){
        this.notifierRequest = notif;
    }


    @Override
    public boolean fire(String executionId) {
        try {
            log.debug("About to fire a GET request ");
            UriBuilder builder = UriBuilder.fromPath(notifierRequest.getReturn_url());
            if(notifierRequest.getPayload() != null) {
                builder.queryParam("payload", notifierRequest.getPayload());
            }

            builder.queryParam("execution_id", executionId);
            URI url = builder.build();

            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(url)
                    .timeout(Duration.ofSeconds(3))
                    .build();


            InetSocketAddress socketAddress = new InetSocketAddress(url.getHost(), url.getPort());
            Socket socket = new Socket();
            socket.connect(socketAddress, 5000); // 5 second connection timeout

            HttpClient client = HttpClient.newBuilder()
                    .version(HTTP_VERSION)
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            ExecutionRequest exec = new ExecutionRequest(executionId, notifierRequest.getExternal_id(), Calendar.getInstance().getTimeInMillis());
            DBhibernetManager.getInstance().savePreExecution(exec);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response == null ? -1 : response.statusCode();
            exec.setStatusCode(statusCode);
            DBhibernetManager.getInstance().savePostExecution(exec);
            DBhibernetManager.getInstance().countExecutions(notifierRequest.getExternal_id());


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

    @Override
    public Long getDelay() {
       return notifierRequest.getDelay();
    }

    @Override
    public Long getInterval() {
       return notifierRequest.getInterval();
    }

    @Override
    public Integer getOccurrence() {
        return notifierRequest.getOccurrences();
    }
}
