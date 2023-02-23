package interfaces;

public interface I_NotifierRequest {
    public boolean fire();

    public Long getDelay();

    public Long getInterval();
}
