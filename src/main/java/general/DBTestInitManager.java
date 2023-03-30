package general;

import com.example.demo.ConfigurationManager;
import enums.eProcess;

import java.util.Date;
import java.util.TimeZone;

public class DBTestInitManager {


    public static void main(String [] args){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.out.println(new Date());
    }

    public static void initDB(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        ConfigurationManager conf = ConfigurationManager.getInstance();
        if(!conf.isLoaded()){

            System.exit(-1);
        }
        try {
            managers.DBManager.getInstance().setConfiguration(conf.getDBConfiguration(eProcess.ENDPOINTS_PROCESS));
        } catch (DBConfigurationException e) {

            e.printStackTrace();
            System.exit(-1);
        }
    }

}