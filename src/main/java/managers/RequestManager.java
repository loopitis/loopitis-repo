package managers;

import com.example.demo.DemoApplication;
import ent.HttpNotifierRequestEntity;
import importers.RequestImporter;
import pojos.HttpNotifierRequest;

public class RequestManager {

    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(DemoApplication.MY_LOGGER);

    private static RequestManager _instance;


    public static synchronized RequestManager getInstance(){
        if(_instance == null){
            _instance = new RequestManager();
        }
        return _instance;
    }


    public Long saveRequest(HttpNotifierRequestEntity notif) {
        RequestImporter importer = new RequestImporter();
        return importer.saveRequest(notif);
    }
}
