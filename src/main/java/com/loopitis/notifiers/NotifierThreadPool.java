package com.loopitis.notifiers;

import com.loopitis.endpoints.ConfigurationManager;
import com.loopitis.enums.eEvent;
import com.loopitis.enums.eRequestStatus;
import com.loopitis.general.FutureCancel;
import com.loopitis.general.HttpNotifier;
import com.loopitis.managers.DBManager;
import com.loopitis.managers.EventManager;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class NotifierThreadPool {
    private final static int NUMBER_OF_THREADS_FOR_PROCESS = ConfigurationManager.getInstance().getNumberOfThreadsForProcess();
    private static NotifierThreadPool instance;
    private ScheduledExecutorService pool = Executors.newScheduledThreadPool(NUMBER_OF_THREADS_FOR_PROCESS);

    private NotifierThreadPool() {
    }

    public synchronized static NotifierThreadPool getInstance() {
        if (instance == null) {
            instance = new NotifierThreadPool();
        }
        return instance;
    }

    public FutureCancel assignTask(HttpNotifier request) {
        FutureCancel futureCancel = new FutureCancel();

        ScheduledFuture<?> future = pool.scheduleAtFixedRate(new Runnable() {
            int num = 0;
            Integer maxTimes = request.getOccurrence() == null ? 1 : request.getOccurrence();

            @Override
            public void run() {
                try {
                    EventManager.getInstance().fire(eEvent.REQUEST_STARTED, request.getRequestId());
                    if (num >= maxTimes) {
                        futureCancel.cancel();
                        DBManager.getInstance().updateStatus(request.getRequestId(), eRequestStatus.FINISHED);
                        EventManager.getInstance().fire(eEvent.REQUEST_FINISHED, request.getRequestId());
                        return;
                    }
                    UUID uuid = UUID.randomUUID();
                    String executionId = uuid.toString();
                    request.fire(executionId, num + 1);
                    num++;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    num++;//so it won't be stuck forever
                }
            }
        }, request.getDelay(), request.getInterval(), TimeUnit.MILLISECONDS);
        if (future == null) return null;
        futureCancel.setFuture(future);
        return futureCancel;
    }
}
