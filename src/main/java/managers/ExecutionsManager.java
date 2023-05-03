package managers;

import com.example.demo.LoopitisApplication;
import consumer.ExecutionRequest;
import filters.ExecutionsFilter;

import java.util.List;

public class ExecutionsManager {
    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(LoopitisApplication.MY_LOGGER);

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
