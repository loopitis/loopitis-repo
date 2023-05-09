package general;

import enums.eRequestStatus;

public class ShowRequestsRequest {

    private eRequestStatus status;

    private String requestId;

    public ShowRequestsRequest() {
    }


    public eRequestStatus getStatus() {
        return status;
    }

    public void setStatus(eRequestStatus status) {
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
