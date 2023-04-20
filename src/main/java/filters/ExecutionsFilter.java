package filters;

public class ExecutionsFilter {
    private String requestId;
    private Integer limit;

    public ExecutionsFilter withRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getRequestId(){
        return this.requestId;
    }

    public ExecutionsFilter withLimit(Integer i) {
        this.limit = i;
        return this;
    }

    public Integer getLimit(){
        return this.limit;
    }
}
