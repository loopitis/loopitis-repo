package com.example.demo;

import aws.S3FileReader;
import enums.eProcess;
import general.DBConfiguration;
import general.NotifierConstants;

import java.io.*;
import java.util.*;


public class ConfigurationManager {
    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(ConfigurationManager.class);
    private static Map<String, String> _configuration;

    private static ConfigurationManager _instance;

    private ConfigurationManager() {
        setConfiguration();
    }

    public synchronized static ConfigurationManager getInstance() {
        if (_instance == null) {
            _instance = new ConfigurationManager();
        }
        return _instance;
    }

    public static void main(String[] args) {
        System.out.println(ConfigurationManager.getInstance().getKafkaTopic());
    }

    public boolean setConfiguration() {
        HashMap<String, String> map = buildMap();
        if (map == null) {
            log.error("Failed building the configuration map. app will shut down");
            return false;
        }
        _configuration = Collections.unmodifiableMap(map);
        return true;

    }

    private HashMap<String, String> buildMap() {
        Properties prop = loadProperties();
        if (prop == null) {
            log.error("Configuration properties could not be loaded - application needs to shutdown");
            return null;
        }
        HashMap<String, String> keyValue = new HashMap<>();
        Iterator<Object> propIter = prop.keySet().iterator();
        String nextKey = null;
        while (propIter.hasNext()) {
            try {
                nextKey = (String) propIter.next();
                keyValue.put(nextKey, prop.getProperty(nextKey));
            } catch (Exception ex) {
                log.error("Failed adding configuration key value " + nextKey, ex);
                ex.printStackTrace();
            }

        }

        return keyValue;
    }


    public Properties loadProperties() {

        Properties prop = new Properties();
        BufferedReader input = null;
        String filename = "config.properties";
        String path = null;

        try {
            path = System.getProperty("user.home");
            path += File.separator + "notifier"+File.separator ;
            File customDir = new File(path);


            if (customDir.exists()) {//if  user/conf does not exists create `one
                log.debug(customDir + " already exists");
            } else if (customDir.mkdirs()) {
                System.out.println(customDir + " was created");
                //get the file from S3
                S3FileReader reader = new S3FileReader();
                reader.downloadConfigFromS3(path);

            } else {
                System.out.println(customDir + " was not created");
            }

            FileInputStream fis = new FileInputStream(path + filename);
            input = new BufferedReader(new InputStreamReader(fis));


            //load a properties file from class path, inside static method
            prop.load(input);
//		prop.put("db_server_name", "dbdemo-encrypted.cudzqnm4gvwx.us-west-2.rds.amazonaws.com");
//		prop.put("isDev",false);
            return prop;
        } catch (FileNotFoundException ex) {
            log.error("File config.properties not found in " + path + File.separator + filename, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public boolean isLoaded() {
        return _configuration != null;
    }

    public String getKafkaTopic() {
        return _configuration.get(NotifierConstants.KAFKA_TASKS_TOPIC_NAME);
    }


    public DBConfiguration getDBConfiguration(eProcess process) {
        DBConfiguration dbConf = getBasicDBConfiguration();
        switch(process){
            case ENDPOINTS_PROCESS:
                dbConf.set_maxPoolSize(Integer.valueOf(_configuration.get(NotifierConstants.CONF_DB_ENDPOINTS_CONNECTION_PULL_MAX_SIZE)));
                return dbConf;
            default:
                dbConf.set_maxPoolSize(NotifierConstants.CONF_DB_DEFAULT_CONNECTION_PULL_MAX_SIZE);
                return dbConf;
        }
    }

    public DBConfiguration getBasicDBConfiguration() {
        try{
            DBConfiguration dbConf = new DBConfiguration();
            dbConf.set_dbName(_configuration.get(NotifierConstants.CONF_DB_NAME));
            dbConf.set_dbHost(_configuration.get(NotifierConstants.CONF_DB_SERVER_NAME));
            dbConf.set_dbPassword(_configuration.get(NotifierConstants.CONF_DB_PASSWORD));
            dbConf.set_dbUser(_configuration.get(NotifierConstants.CONF_DB_USER));
            dbConf.set_dbPort(Integer.valueOf(_configuration.get(NotifierConstants.CONF_DB_PORT)));
            dbConf.setReadOnly(Boolean.valueOf(_configuration.get(NotifierConstants.CONF_DB_READ_ONLY_MODE)));
            return dbConf;
        }
        catch(Exception ex){
            log.error("error while trying to extract configuration data - check if the configuration.properties is valid",ex);
            return null;
        }
    }

    public boolean isDevelpmentEnv() {
        return Boolean.valueOf(_configuration.get(NotifierConstants.CONF_IS_DEVELOPMENT));
    }
}
