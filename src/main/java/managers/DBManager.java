package managers;

import com.example.demo.ConfigurationManager;
import enums.eProcess;
import general.DBConfiguration;
import general.DBConfigurationException;
import general.DBTestInitManager;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBManager {
    private static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(DBManager.class);

    private static boolean DB_READ_ONLY = false;

    private static DBManager dbManager = null;

    private String DB_USER = "";

    private String DB_PASSWORD = "";

    private BasicDataSource _connectionPool;

    private static int DB_MAX_CONNECTIONS = -1;

    private static String DB_NAME = "";

    private static int DB_PORT_NUMBER = -1;

    private static String DB_SERVER_HOST = "";
//	private static final String DB_SERVER_NAME = "52.27.211.17";


    private DBManager() {
        try {
            System.out.println("DB manager initted");
            Class.forName("org.postgresql.Driver");
            if (_connectionPool == null) {
                setConfiguration(ConfigurationManager.getInstance().getDBConfiguration(eProcess.ENDPOINTS_PROCESS));
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setConfiguration(DBConfiguration dbConfiguration) throws DBConfigurationException {
        if (dbConfiguration == null) {
            log.error("configuration is null could not init the DB- FATAL");
            return;
        }

        DB_SERVER_HOST = dbConfiguration.get_dbHost();
        DB_USER = dbConfiguration.get_dbUser();
        DB_PASSWORD = dbConfiguration.get_dbPassword();
        DB_MAX_CONNECTIONS = dbConfiguration.get_maxPoolSize();

        initConnection();
    }

    private void createDbPoolConnection() {

        _connectionPool = new BasicDataSource();
//        _connectionPool.setd("QuantifyAPI");
//        _connectionPool.setApplicationName(DB_SERVER_NAME);
        _connectionPool.setUrl(DB_SERVER_HOST);
        _connectionPool.setUsername(DB_USER);
        _connectionPool.setPassword(DB_PASSWORD);
        _connectionPool.setMaxTotal(DB_MAX_CONNECTIONS);
        _connectionPool.setInitialSize(3);


    }

    public boolean isDBName(String prodDB) {
        return DB_NAME != null && DB_NAME.equals(prodDB);
    }


    public static synchronized DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }

    private void initConnection() throws DBConfigurationException {
        log.debug("******************************************");
        log.debug("***********  CONNECTING TO DB  ***********");
        log.debug("*********   " + "DB:" + DB_NAME + " at " + DB_SERVER_HOST + "   ***********");
        log.debug("******************************************");

        createDbPoolConnection();
        testConnection();
    }

    public boolean checkDBAndReconnectIfNeeded() {
        try {
            testConnection();
            return true;
        } catch (DBConfigurationException e) {
            log.error("DB Connection failed trying to reopen connection");
            DBTestInitManager.initDB();
            return true;
        }
    }

    private void testConnection() throws DBConfigurationException {
        Connection onLineDbconnection = null;
        try {
            onLineDbconnection = _connectionPool.getConnection();
            log.debug("DB test connection passed db name " + DB_NAME + " on " + DB_SERVER_HOST);
            return;
        } catch (SQLException ex) {
            log.error("DB test connection has failed", ex);
            throw new DBConfigurationException("Test DB connection has failed - check configuration file!");
        } finally {
            if (onLineDbconnection != null) {
                try {
                    onLineDbconnection.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }
}