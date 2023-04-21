package pojos;

import enums.eCallbackType;

public class HttpNotifierRequest {
    private String return_url;
    private Long delay;
    private Long interval;
    private String payload;
    private Long id;
    private Integer occurrences;

    private String external_id;

    private Long internal_id;

    private String name;

    private eCallbackType callback_type;


    public HttpNotifierRequest(){}

    public HttpNotifierRequest(String returnUrl, Long delay, Long interval, String payload, Long id, Integer occurance, eCallbackType callbackType) {
        return_url = returnUrl;
        this.delay = delay;
        this.interval = interval;
        this.payload = payload;
        this.id = id;
        this.occurrences = occurance;
        this.callback_type = callbackType;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public void setInternal_id(Long internal_id) {
        this.internal_id = internal_id;
    }

    public String getExternal_id() {
        return external_id;
    }

    public Long getInternal_id() {
        return internal_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOccurrences() {
        return occurrences;
    }

    public eCallbackType getCallback_type() {
        return callback_type;
    }

    public void setCallback_type(eCallbackType callback_type) {
        this.callback_type = callback_type;
    }

    public void setOccurrences(Integer occurrences) {
        this.occurrences = occurrences;
    }
}




