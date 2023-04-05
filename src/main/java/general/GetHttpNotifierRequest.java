package general;

import interfaces.I_NotifierRequest;
import pojos.HttpNotifierRequest;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class GetHttpNotifierRequest implements I_NotifierRequest {
    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(GetHttpNotifierRequest.class);

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
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            log.info(response.statusCode());
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
