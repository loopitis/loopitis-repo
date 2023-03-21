package consumer;

import interfaces.I_NotifierRequest;
import notifiers.NotifierThreadPool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotifierTask {

    private I_NotifierRequest request;

    public NotifierTask(I_NotifierRequest req){
        this.request = req;
    }


    public void handle(){
        NotifierThreadPool.getInstance().assignTask(request);
    }
}
