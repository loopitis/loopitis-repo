package interfaces;

public interface I_NotifierRequest {

    public String getRequestId();

    public boolean fire(String executionId);

    public Long getDelay();

    public Long getInterval();

    public Integer getOccurrence();
}
