package general;

public class ExecutionResponse {

    private String showRequest;
    private Integer requestedTimes;
    private String parentRequestId;
    private String parentRequestName;
    private Integer executionNumer;
    private String executionId;
    private String payload;
    private String canceLink;

    public ExecutionResponse(){}

    public ExecutionResponse(String executionId, int executionNumber, String payload, Integer occurrences, String requestId, String name, String canceLink, String showRequest) {
        this.executionId = executionId;
        this.payload = payload;
        this.requestedTimes = occurrences;
        this.executionNumer = executionNumber;
        this.parentRequestId = requestId;
        this.parentRequestName = name;
        this.canceLink = canceLink;
        this.showRequest = showRequest;

    }



    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Integer getExecutionNumer() {
        return executionNumer;
    }

    public void setExecutionNumer(Integer executionNumer) {
        this.executionNumer = executionNumer;
    }

    public Integer getRequestedTimes() {
        return requestedTimes;
    }

    public void setRequestedTimes(Integer requestedTimes) {
        this.requestedTimes = requestedTimes;
    }

    public String getParentRequestId() {
        return parentRequestId;
    }

    public void setParentRequestId(String parentRequestId) {
        this.parentRequestId = parentRequestId;
    }

    public String getParentRequestName() {
        return parentRequestName;
    }

    public void setParentRequestName(String parentRequestName) {
        this.parentRequestName = parentRequestName;
    }

    public String getCanceLink() {
        return canceLink;
    }

    public void setCanceLink(String canceLink) {
        this.canceLink = canceLink;
    }

    public String getShowRequest() {
        return showRequest;
    }

    public void setShowRequest(String showRequest) {
        this.showRequest = showRequest;
    }
}
