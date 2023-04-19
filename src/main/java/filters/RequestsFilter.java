package filters;

import enums.eRequestStatus;
import org.springframework.http.ResponseEntity;

public class RequestsFilter {

    private eRequestStatus status;

    public RequestsFilter withStatus(eRequestStatus status){
        this.status = status;
        return this;
    }

    public eRequestStatus getStatus() {
        return status;
    }
}
