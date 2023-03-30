package pojos;

public class HttpNotifierRequest {
    private String return_url;
    private Long delay;
    private Long interval;
    private String payload;
    private Integer id;
    private Integer occurrence;

    private String external_id;

    private Integer internal_id;

    public HttpNotifierRequest(){}

    public HttpNotifierRequest(String returnUrl, Long delay, Long interval, String payload, Integer id, Integer occurance) {
        return_url = returnUrl;
        this.delay = delay;
        this.interval = interval;
        this.payload = payload;
        this.id = id;
        this.occurrence = occurance;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(Integer occurrence) {
        this.occurrence = occurrence;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public void setInternal_id(Integer internal_id) {
        this.internal_id = internal_id;
    }

    public String getExternal_id() {
        return external_id;
    }

    public Integer getInternal_id() {
        return internal_id;
    }
}




