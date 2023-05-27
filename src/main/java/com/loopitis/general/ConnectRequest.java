package com.loopitis.general;

public class ConnectRequest {
    private String url;

    public ConnectRequest() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ConnectRequest{" +
                "url='" + url + '\'' +
                '}';
    }
}
