package filters;

import enums.eRequestStatus;

public class RequestsFilter {

    private eRequestStatus status;
    private Integer limit;

    public RequestsFilter withStatus(eRequestStatus status){
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
}
