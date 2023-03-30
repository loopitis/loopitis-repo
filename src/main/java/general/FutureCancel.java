package general;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

public class FutureCancel {
    Future<?>  future;
    private boolean canceled = false;

    public void cancel() {
        canceled = true;

        if (future != null){
            future.cancel(true);
        }
    }

    public void setFuture(ScheduledFuture<?> future) {
        this.future = future;
        if(canceled){
            future.cancel(true);
        }
    }
}
