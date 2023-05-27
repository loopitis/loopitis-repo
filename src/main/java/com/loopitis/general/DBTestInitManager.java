package com.loopitis.general;

import com.loopitis.endpoints.ConfigurationManager;
import com.loopitis.managers.DBManager;
import com.loopitis.enums.eProcess;

import java.util.Date;
import java.util.TimeZone;

public class DBTestInitManager {


    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.out.println(new Date());
    }

    public static void initDB() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        ConfigurationManager conf = ConfigurationManager.getInstance();
        if (!conf.isLoaded()) {

            System.exit(-1);
        }
        try {
            DBManager.getInstance().setConfiguration(conf.getDBConfiguration(eProcess.ENDPOINTS_PROCESS));
        } catch (DBConfigurationException e) {

            e.printStackTrace();
            System.exit(-1);
        }
    }

}