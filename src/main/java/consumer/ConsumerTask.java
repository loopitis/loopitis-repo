package consumer;

import interfaces.I_NotifierRequest;
import managers.NotifierManager;
import pojos.HttpNotifierRequest;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConsumerTask {

    //temporary
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    private I_NotifierRequest request;

    public ConsumerTask(I_NotifierRequest req){
        this.request = req;
    }


    public void handle(){

        scheduler.scheduleAtFixedRate(()-> NotifierManager.getInstance().fire(request), request.getDelay(), request.getInterval(), TimeUnit.MILLISECONDS);
    }
}
