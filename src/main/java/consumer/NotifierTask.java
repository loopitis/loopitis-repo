package consumer;

import general.FutureCancel;
import general.HttpNotifier;
import notifiers.NotifierThreadPool;

public class NotifierTask {

    private HttpNotifier request;

    public NotifierTask(HttpNotifier req) {
        this.request = req;
    }


    public FutureCancel handle() {
        return NotifierThreadPool.getInstance().assignTask(request);
    }
}
