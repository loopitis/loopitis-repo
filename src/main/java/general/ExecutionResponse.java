package general;

public class ExecutionResponse {

    private String executionId;
    private String payload;

    public ExecutionResponse(){}
    public ExecutionResponse(String executionId, String payload) {
        this.executionId = executionId;
        this.payload = payload;
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
}
