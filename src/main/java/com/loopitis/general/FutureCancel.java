package com.loopitis.general;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

public class FutureCancel {
    Future<?> future;
    private boolean canceled = false;

    public boolean cancel() {
        canceled = true;

        if (future != null) {
            return future.cancel(true);
        }
        return false;
    }

    public void setFuture(ScheduledFuture<?> future) {
        this.future = future;
        if (canceled) {
            future.cancel(true);
        }
    }
}
