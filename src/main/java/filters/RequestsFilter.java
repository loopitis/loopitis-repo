package filters;

import enums.eRequestStatus;

public class RequestsFilter {

    private eRequestStatus status;
    private Integer limit;
    private String requestId;

    public RequestsFilter withStatus(eRequestStatus status) {
        this.status = status;
        return this;
    }

    public eRequestStatus getStatus() {
        return status;
    }

    public RequestsFilter withLimit(Integer i) {
        this.limit = i;
        return this;
    }

    public Integer getLimit() {
        return this.limit;
    }

    public RequestsFilter withRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getRequestId() {
        return this.requestId;
    }
}
