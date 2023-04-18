package general;

import interfaces.I_NotifierRequest;
import pojos.HttpNotifierRequest;

public class PostHttpNotifierRequest implements I_NotifierRequest {
    private HttpNotifierRequest httpRequest;

    public PostHttpNotifierRequest(){}

    @Override
    public String getRequestId() {
        return httpRequest.getExternal_id();
    }

    @Override
    public boolean fire(String executionId) {
        return false;
    }

    @Override
    public Long getDelay() {
        return httpRequest.getDelay();
    }

    @Override
    public Long getInterval() {
        return httpRequest.getInterval();
    }

    @Override
    public Integer getOccurrence() {
        return httpRequest.getOccurrences();
    }
}
