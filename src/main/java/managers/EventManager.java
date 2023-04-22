package managers;

import com.example.demo.DemoApplication;
import com.google.gson.Gson;
import enums.eEvent;
import general.EventNoticiationData;
import services.RESTServices;

import java.util.Map;

public class EventManager {
    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(DemoApplication.MY_LOGGER);

    private static EventManager _instance;

    private Gson g = new Gson();

    public static synchronized EventManager getInstance(){
        if(_instance == null){
            _instance = new EventManager();
        }
        return _instance;
    }

    public void fire(eEvent event, String data) {
        var listeners = RedisManager.getInstance().readAllListeners();
        for(Map.Entry<String, String> entry : listeners){
            String urlToNotify = entry.getKey();
            EventNoticiationData notificationData = new EventNoticiationData(event, data);
            var response = RESTServices.POST(urlToNotify, g.toJson(notificationData));
            if(response.statusCode() != 200){
                log.debug("Failure listener - listener fired "+event+" response "+response.statusCode());
                //update redis with the new data if //update redis with the new data if there were more then 5 attempts , remove the listener from redisthere were more then 5 attempts , remove the listener from redis

                return;
            }
            log.debug("listener fired "+event+" response "+response.statusCode());
        }
        //get listener url and failures from redis



    }

}
