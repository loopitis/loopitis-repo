package notifiers;

import com.example.demo.ConfigurationManager;
import enums.eRequestStatus;
import general.FutureCancel;
import interfaces.I_NotifierRequest;
import managers.DBhibernetManager;

import java.util.UUID;
import java.util.concurrent.*;

public class NotifierThreadPool {
    private static NotifierThreadPool instance;

    private final static int NUMBER_OF_THREADS_FOR_PROCESS = ConfigurationManager.getInstance().getNumberOfThreadsForProcess();
    private ScheduledExecutorService pool = Executors.newScheduledThreadPool(NUMBER_OF_THREADS_FOR_PROCESS);
    private NotifierThreadPool(){}

    public synchronized static NotifierThreadPool getInstance(){
        if(instance == null){
            instance = new NotifierThreadPool();
        }
        return instance;
    }

    public FutureCancel assignTask(I_NotifierRequest request) {
        FutureCancel futureCancel = new FutureCancel();

        ScheduledFuture<?> future = pool.scheduleAtFixedRate(new Runnable() {
            int num = 0;
            Integer maxTimes = request.getOccurrence() == null ?  1: request.getOccurrence();

            @Override
            public void run() {

                if(num >= maxTimes){
                    futureCancel.cancel();
                    DBhibernetManager.getInstance().updateStatus(request.getRequestId(), eRequestStatus.FINISHED);
                    return;
                }
                UUID uuid = UUID.randomUUID();
                String executionId = uuid.toString();
                request.fire(executionId);
                num++;
            }
        }, request.getDelay(), request.getInterval(), TimeUnit.MILLISECONDS);
        if(future == null) return null;
        futureCancel.setFuture(future);
        return futureCancel;
    }
}
