package notifiers;

import interfaces.I_NotifierRequest;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NotifierThreadPool {
    private static NotifierThreadPool instance;
    private ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);
    private NotifierThreadPool(){}

    public synchronized static NotifierThreadPool getInstance(){
        if(instance == null){
            instance = new NotifierThreadPool();
        }
        return instance;
    }

    public void assignTask(I_NotifierRequest request) {
        pool.scheduleAtFixedRate(()->request.fire(), request.getDelay(), request.getInterval(), TimeUnit.MILLISECONDS);
    }
}
