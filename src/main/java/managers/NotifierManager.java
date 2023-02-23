package managers;

import interfaces.I_NotifierRequest;
import pojos.HttpNotifierRequest;

public class NotifierManager {

    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(RedisManager.class);

    private static NotifierManager _instance;


    public static synchronized NotifierManager getInstance(){
        if(_instance == null){
            _instance = new NotifierManager();
        }
        return _instance;
    }

    public void fire(I_NotifierRequest request) {
        request.fire();
    }
}
