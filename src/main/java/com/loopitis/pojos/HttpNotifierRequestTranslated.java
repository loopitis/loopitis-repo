package com.loopitis.pojos;

import com.loopitis.services.Services;
import com.loopitis.enums.eCallbackType;

public class HttpNotifierRequestTranslated {
    private String return_url;
    private String notify_status_not_ok;
    private Long delay;
    private Long interval;
    private String payload;
    private Long id;
    private Integer occurrences;

    private String external_id;

    private Long internal_id;

    private String name;

    private eCallbackType callback_type;


    public HttpNotifierRequestTranslated() {
    }

    public HttpNotifierRequestTranslated(HttpNotifierRequest request) {
        this.name = request.getName();
        this.occurrences = request.getOccurrences();
        this.external_id = request.getExternal_id();
        this.callback_type = request.getCallback_type();
        this.return_url = request.getReturn_url();
        this.notify_status_not_ok = request.getNotify_status_not_ok();
        this.payload = request.getPayload();
        this.id = request.getId();
        this.internal_id = request.getInternal_id();
        this.delay = Services.convertToMilliseconds(request.getDelay());
        this.interval = Services.convertToMilliseconds(request.getInterval());
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
