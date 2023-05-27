package com.loopitis.managers;

import com.loopitis.endpoints.LoopitisApplication;
import com.loopitis.ent.HttpNotifierRequestEntity;
import com.loopitis.importers.RequestImporter;

public class RequestManager {

    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(LoopitisApplication.MY_LOGGER);

    private static RequestManager _instance;


    public static synchronized RequestManager getInstance() {
        if (_instance == null) {
            _instance = new RequestManager();
        }
        return _instance;
    }


    public Long saveRequest(HttpNotifierRequestEntity notif) {
        RequestImporter importer = new RequestImporter();
        return importer.saveRequest(notif);
    }
}
