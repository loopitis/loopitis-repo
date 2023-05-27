package com.loopitis.managers;

import com.loopitis.endpoints.LoopitisApplication;
import com.google.gson.Gson;
import com.loopitis.enums.eEvent;
import com.loopitis.general.ClientConnectionDetails;
import com.loopitis.general.EventNoticiationData;
import com.loopitis.services.RESTServices;

import java.net.http.HttpResponse;
import java.util.Map;

public class EventManager {
    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(LoopitisApplication.MY_LOGGER);

    private static EventManager _instance;

    private Gson g = new Gson();

    public static synchronized EventManager getInstance() {
        if (_instance == null) {
            _instance = new EventManager();
        }
        return _instance;
    }

    public void fire(eEvent event, String data) {
        var listeners = RedisManager.getInstance().readAllListeners();

        if (listeners == null || listeners.isEmpty()) {
            log.debug("Could not find any listeners for any event " + event);
            return;
        }

        for (Map.Entry<String, String> entry : listeners) {
            String urlToNotify = entry.getKey();
            String value = entry.getValue();
            EventNoticiationData notificationData = new EventNoticiationData(event, data);
            HttpResponse<String> response = null;
            try {
                response = RESTServices.POST(urlToNotify, g.toJson(notificationData));
            } catch (Exception ex) {
                log.debug("Failed announcing the client of " + event + " error: " + ex.getMessage());

            }
            if (response == null || response.statusCode() != 200) {
                log.debug("Failure listener - listener fired " + event);
                //update redis with the new data if //update redis with the new data if there were more then 5 attempts , remove the listener from redisthere were more then 5 attempts , remove the listener from redis
                ClientConnectionDetails details = g.fromJson(value, ClientConnectionDetails.class);
                if (details.getFailures() > 5) {
                    log.debug("Remove listener from listener list");
                    RedisManager.getInstance().removeListener(urlToNotify);
                } else {
                    log.debug("Client listener failure #" + details.getFailures() + " when it gets to 5 we will remove the listener");
                    details.setFailures(details.getFailures() + 1);
                    RedisManager.getInstance().addListener(urlToNotify, g.toJson(details));
                }

            }
        }
        //get listener url and failures from redis


    }

}
