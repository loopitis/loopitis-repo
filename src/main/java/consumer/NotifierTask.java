package consumer;

import general.FutureCancel;
import interfaces.I_NotifierRequest;
import notifiers.NotifierThreadPool;

public class NotifierTask {

    private I_NotifierRequest request;

    public NotifierTask(I_NotifierRequest req){
        this.request = req;
    }


    public FutureCancel handle(){
        return NotifierThreadPool.getInstance().assignTask(request);
    }
}
