package managers;

import com.example.demo.LoopitisApplication;
import ent.HttpNotifierRequestEntity;
import filters.RequestsFilter;

import java.util.List;

public class RequestsManager {
    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(LoopitisApplication.MY_LOGGER);

    private static RequestsManager _instance;


    public static synchronized RequestsManager getInstance() {
        if (_instance == null) {
            _instance = new RequestsManager();
        }
        return _instance;
    }

    public List<HttpNotifierRequestEntity> getRequests(RequestsFilter filter) {
        return DBManager.getInstance().getRequests(filter);
    }
}
