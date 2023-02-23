package general;

import interfaces.I_NotifierRequest;
import managers.RedisManager;
import pojos.HttpNotifierRequest;

import java.net.URI;
import java.net.http.HttpRequest;

public class GetHttpNotifierRequest implements I_NotifierRequest {
    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(GetHttpNotifierRequest.class);

    private HttpNotifierRequest notifierRequest;

    public GetHttpNotifierRequest(HttpNotifierRequest notif){
        this.notifierRequest = notif;
    }


    @Override
    public boolean fire() {
        try {
            log.debug("About to fire a GET request ");
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(notifierRequest.getReturn_url()))
                    .build();
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
}
