package general;

public class CancelTaskRequest {
    private String requestId;

    public CancelTaskRequest(){}

    public CancelTaskRequest(String requestId){
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
