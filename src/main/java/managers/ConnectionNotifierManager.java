package managers;

import com.example.demo.LoopitisApplication;
import com.google.gson.Gson;
import enums.eEvent;
import general.ClientConnectionDetails;
import general.ConnectRequest;

import java.util.List;

public class ConnectionNotifierManager {
    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(LoopitisApplication.MY_LOGGER);
    private Gson g = new Gson();

    private static ConnectionNotifierManager _instance;


    public static synchronized ConnectionNotifierManager getInstance(){
        if(_instance == null){
            _instance = new ConnectionNotifierManager();
        }
        return _instance;
    }


    public void saveConnectionRequest(ConnectRequest connectRequest, List<eEvent> eventsToListen) {
        //save to DB later
        String urlToNotify = connectRequest.getUrl();
        //update redis with this url as key and a json with data as value {"eventsRegistration":[], "failures_in_row":4,}
        ClientConnectionDetails details = new ClientConnectionDetails(eventsToListen);
        RedisManager.getInstance().removeAllListeners();//currently supporting only one listener
        RedisManager.getInstance().addListener(urlToNotify, g.toJson(details));
    }


}
