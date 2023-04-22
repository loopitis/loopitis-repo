package managers;

import com.example.demo.DemoApplication;
import com.google.gson.Gson;
import consumer.ExecutionRequest;
import filters.ExecutionsFilter;

import java.util.List;

public class ExecutionsManager {
    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(DemoApplication.MY_LOGGER);

    private static ExecutionsManager _instance;



    public static synchronized ExecutionsManager getInstance(){
        if(_instance == null){
            _instance = new ExecutionsManager();
        }
        return _instance;
    }

    public List<ExecutionRequest> getExecutions(ExecutionsFilter filter) {
        return DBhibernetManager.getInstance().getExecutions(filter);
    }
}
