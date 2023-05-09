package general;

public class ExecutionResponse {

    private Integer requestedTimes;
    private String parentRequestId;
    private String parentRequestName;
    private Integer executionNumber;
    private String executionId;
    private String payload;
    private String showRequest;
    private String cancelLink;

    public ExecutionResponse() {
    }

    public ExecutionResponse(String executionId, int executionNumber, String payload, Integer occurrences, String requestId, String name, String cancelLink, String showRequest) {
        this.executionId = executionId;
        this.payload = payload;
        this.requestedTimes = occurrences;
        this.executionNumber = executionNumber;
        this.parentRequestId = requestId;
        this.parentRequestName = name;
        this.cancelLink = cancelLink;
        this.showRequest = showRequest;

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

    public Integer getExecutionNumber() {
        return executionNumber;
    }

    public void setExecutionNumber(Integer executionNumber) {
        this.executionNumber = executionNumber;
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

    public String getShowRequest() {
        return showRequest;
    }

    public void setShowRequest(String showRequest) {
        this.showRequest = showRequest;
    }

    public String getCancelLink() {
        return cancelLink;
    }

    public void setCancelLink(String cancelLink) {
        this.cancelLink = cancelLink;
    }
}
