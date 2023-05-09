package pojos;

import enums.eCallbackType;

public class HttpNotifierRequest {
    private String return_url;
    private String notify_status_not_ok;
    private String delay;
    private String interval;
    private String payload;
    private Long id;
    private Integer occurrences;

    private String external_id;

    private Long internal_id;

    private String name;

    private eCallbackType callback_type;


    public HttpNotifierRequest() {
    }

    public HttpNotifierRequest(String returnUrl, String delay, String interval, String payload, Long id, Integer occurance, eCallbackType callbackType) {
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


    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
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

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public Long getInternal_id() {
        return internal_id;
    }

    public void setInternal_id(Long internal_id) {
        this.internal_id = internal_id;
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

    public void setOccurrences(Integer occurrences) {
        this.occurrences = occurrences;
    }

    public eCallbackType getCallback_type() {
        return callback_type;
    }

    public void setCallback_type(eCallbackType callback_type) {
        this.callback_type = callback_type;
    }

    public String getNotify_status_not_ok() {
        return notify_status_not_ok;
    }

    public void setNotify_status_not_ok(String notify_status_not_ok) {
        this.notify_status_not_ok = notify_status_not_ok;
    }
}




