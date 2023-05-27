package com.loopitis.consumer;

import com.loopitis.general.FutureCancel;
import com.loopitis.general.HttpNotifier;
import com.loopitis.notifiers.NotifierThreadPool;

public class NotifierTask {

    private HttpNotifier request;

    public NotifierTask(HttpNotifier req) {
        this.request = req;
    }


    public FutureCancel handle() {
        return NotifierThreadPool.getInstance().assignTask(request);
    }
}
