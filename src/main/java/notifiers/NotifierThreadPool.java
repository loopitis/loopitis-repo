package notifiers;

import general.FutureCancel;
import interfaces.I_NotifierRequest;

import java.util.concurrent.*;

public class NotifierThreadPool {
    private static NotifierThreadPool instance;
    private ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
    private NotifierThreadPool(){}

    public synchronized static NotifierThreadPool getInstance(){
        if(instance == null){
            instance = new NotifierThreadPool();
        }
        return instance;
    }

    public void assignTask(I_NotifierRequest request) {
        FutureCancel futureCancel = new FutureCancel();

        ScheduledFuture<?> future = pool.scheduleAtFixedRate(new Runnable() {
            int num = 0;
            Integer maxTimes = request.getOccurrence() == null ?  1: request.getOccurrence();

            @Override
            public void run() {

                if(num > maxTimes){
                    futureCancel.cancel();
                    return;
                }
                request.fire();
                num++;
            }
        }, request.getDelay(), request.getInterval(), TimeUnit.MILLISECONDS);

        futureCancel.setFuture(future);
    }
}
