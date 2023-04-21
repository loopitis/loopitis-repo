package general;

import enums.eRequestStatus;

public class ShowRequestsRequest {

    private eRequestStatus status;

    public ShowRequestsRequest(){}


    public eRequestStatus getStatus() {
        return status;
    }

    public void setStatus(eRequestStatus status) {
        this.status = status;
    }
}
