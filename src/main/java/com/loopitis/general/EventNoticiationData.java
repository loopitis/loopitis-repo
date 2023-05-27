package com.loopitis.general;

import com.loopitis.enums.eEvent;

public class EventNoticiationData {

    private eEvent event;

    private String json;


    public EventNoticiationData(eEvent event, String data) {
        this.event = event;
        this.json = data;
    }

    public EventNoticiationData() {
    }

    public eEvent getEvent() {
        return event;
    }

    public void setEvent(eEvent event) {
        this.event = event;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
