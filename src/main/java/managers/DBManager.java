package managers;

import com.example.demo.ConfigurationManager;
import com.example.demo.DemoApplication;
import enums.eProcess;
import general.DBConfiguration;
import general.DBConfigurationException;
import general.DBTestInitManager;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DBManager {
    private static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(DemoApplication.MY_LOGGER);


    private static boolean DB_READ_ONLY = false;

    private static DBManager dbManager = null;

    private String DB_USER = "myUser";

    private String DB_PASSWORD = "mypassword";

    private BasicDataSource _connectionPool;

    private static int DB_MAX_CONNECTIONS = -1;

    private static String DB_NAME = "mydb";

    private static int DB_PORT_NUMBER = 5432;

    private static String DB_SERVER_HOST = "localhost";
    private SessionFactory sessionFactory;

    public static void main(String[] args) throws DBConfigurationException {
//        DBManager i = DBManager.getInstance();
//        i.call();

        // Create a BasicDataSource with database connection details
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/mydb");
        dataSource.setUsername("myusername");
        dataSource.setPassword("mypassword");

        // Create a Hibernate configuration with the datasource
        Configuration configuration = new Configuration()
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .setProperty("hibernate.connection.datasource", "myDataSource");

        // Build the Hibernate SessionFactory
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        // Open a session from the SessionFactory
        Session session = sessionFactory.openSession();

        // Run an SQL query using the session
        String query = "SELECT * FROM my_table";
        List results = session.createNativeQuery(query).list();
        for (Object row : results) {
            System.out.println(row);
        }

        // Close the session and SessionFactory
        session.close();
        sessionFactory.close();
    }



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
        DB_PORT_NUMBER = dbConfiguration.get_dbPort();
        DB_NAME = dbConfiguration.get_dbName();


        initConnection();
    }

    private void createDbPoolConnection() {

        _connectionPool = new BasicDataSource();
//        _connectionPool.set("QuantifyAPI");
//        _connectionPool.setApplicationName(DB_SERVER_NAME);
        _connectionPool.setUrl("jdbc:postgresql://"+DB_SERVER_HOST+":"+DB_PORT_NUMBER+"/"+DB_NAME);

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
//        sessionFactory = new Configuration().configure().buildSessionFactory(new StandardServiceRegistryBuilder()
//                .applySetting(Environment.DATASOURCE, _connectionPool)
//                .build());
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


    private void call() throws DBConfigurationException {
        Connection onLineDbconnection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            onLineDbconnection = _connectionPool.getConnection();
            try {
                statement = onLineDbconnection.createStatement();
                String sql = "SELECT * FROM myTable"; // Replace with your table name
                resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    // Retrieve data from each row and process it
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    System.out.println("ID: " + id + ", Name: " + name );
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (resultSet != null) {
                        resultSet.close();
                    }

                    if (statement != null) {
                        statement.close();
                    }

                    if (_connectionPool != null) {
                        _connectionPool.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }





}