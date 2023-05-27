package com.loopitis.managers;

import com.loopitis.endpoints.LoopitisApplication;
import com.loopitis.consumer.ExecutionRequest;
import com.loopitis.filters.ExecutionsFilter;

import java.util.List;

public class ExecutionsManager {
    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(LoopitisApplication.MY_LOGGER);

    private static ExecutionsManager _instance;


    public static synchronized ExecutionsManager getInstance() {
        if (_instance == null) {
            _instance = new ExecutionsManager();
        }
        return _instance;
    }

    public List<ExecutionRequest> getExecutions(ExecutionsFilter filter) {
        return DBManager.getInstance().getExecutions(filter);
    }
}
