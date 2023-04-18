package managers;

import com.example.demo.ConfigurationManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import consumer.ExecutionRequest;
import ent.HttpNotifierRequestEntity;

import enums.eProcess;
import enums.eRequestStatus;
import general.DBConfiguration;
import general.DBConfigurationException;
import general.DBTestInitManager;
import org.eclipse.persistence.config.PersistenceUnitProperties;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;


public class DBhibernetManager {
    private static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(DBhibernetManager.class);

    private static boolean DB_READ_ONLY = false;

    private static DBhibernetManager dbManager = null;

    private EntityManagerFactory sessionFactory;

    private String DB_USER = "myUser";

    private String DB_PASSWORD = "mypassword";


    private static int DB_MAX_CONNECTIONS = 1;

    private static String DB_NAME = "mydb";

    private static int DB_PORT_NUMBER = 5432;

    private static String DB_SERVER_HOST = "redis";

    public static void main(String[] args) throws JsonProcessingException {
        DBhibernetManager manager = DBhibernetManager.getInstance();

        HttpNotifierRequestEntity entity = new HttpNotifierRequestEntity();
        entity.setExternalId("12345");
        entity.setName("Sample Request");
        entity.setStatus("Pending");
        entity.setReturnUrl("https://example.com/callback");
        entity.setDelay(3000L);
        entity.setInterval(10000L);
        entity.setOccurrences(5);
        entity.setDone(0);
        entity.setPayload("{\"key1\":\"value1\",\"key2\":\"value2\"}");


//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonData = objectMapper.readTree(json);
//
        manager.saveRequest(entity);

//        System.out.println(notifierRequest.getInterval());
//        Person p = new Person("hey", "Jude");
//        p.setId(4);
//        manager.addPerson(p);
//        System.out.println(p);

    }

    private void testSelect() {
        String queryString = "SELECT e FROM Employee e WHERE e.department = :dept";
    }

    private DBhibernetManager() {
        try {
            System.out.println("DB manager initted");
            Class.forName("org.postgresql.Driver");
            if (sessionFactory == null) {
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




    private void getSessionFactory() {
        // Add connection pool
        String jdbc_full_url = "jdbc:postgresql://" + DB_SERVER_HOST + ":" + DB_PORT_NUMBER + "/" + DB_NAME;
        log.debug("Setting db host to ******** : " + jdbc_full_url);
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbc_full_url);
        hikariConfig.setUsername(DB_USER);
        hikariConfig.setPassword(DB_PASSWORD);

        hikariConfig.setDriverClassName("org.postgresql.Driver");

        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(5);
        hikariConfig.setConnectionTimeout(30000);
        hikariConfig.setIdleTimeout(600000);
        hikariConfig.setMaxLifetime(1800000);
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        Map<String, Object> properties = new HashMap<>();
        properties.put(PersistenceUnitProperties.JTA_DATASOURCE, dataSource);
        properties.put(PersistenceUnitProperties.JDBC_DRIVER, "org.postgresql.Driver");
        properties.put(PersistenceUnitProperties.JDBC_URL, jdbc_full_url);
        properties.put(PersistenceUnitProperties.JDBC_USER, DB_USER);
        properties.put(PersistenceUnitProperties.JDBC_PASSWORD, DB_PASSWORD);

        // Configure the EntityManagerFactory to use the HikariCP DataSource
        sessionFactory = Persistence.createEntityManagerFactory("examplePU", properties);


    }




    public boolean isDBName(String prodDB) {
        return DB_NAME != null && DB_NAME.equals(prodDB);
    }


    public static synchronized DBhibernetManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBhibernetManager();
        }
        return dbManager;
    }

    private void initConnection() throws DBConfigurationException {
        log.debug("******************************************");
        log.debug("***********  CONNECTING TO DB  ***********");
        log.debug("*********   " + "DB:" + DB_NAME + " at " + DB_SERVER_HOST + "   ***********");
        log.debug("******************************************");

        getSessionFactory();
//        testConnection();
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
    }

    public void saveRequest(HttpNotifierRequestEntity request) {
        // Obtain an EntityManager instance from EntityManagerFactory
        EntityManager em = sessionFactory.createEntityManager();


        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            em.persist(request);
            tx.commit();
        }
        catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }

        System.out.println(request);
    }


    public void savePreExecution(ExecutionRequest request) {
        EntityManager em = sessionFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            em.persist(request);
            tx.commit();
        }
        catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }

        System.out.println(request);
    }

    public void savePostExecution(ExecutionRequest exec) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        try{
            tx.begin();
            String nativeQuery = "UPDATE notifier.executions set status_code = ? where i_id = ?";

            Query query = entityManager.createNativeQuery(nativeQuery);
            query.setParameter(1, exec.getStatusCode());
            query.setParameter(2, exec.getId());
            int numUpdated = query.executeUpdate();

            tx.commit();
        }
        catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    public void countExecutions(String externalId) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        try{
            tx.begin();
            String nativeQuery = "UPDATE notifier.requests set done = done+1 where e_id = ?";

            Query query = entityManager.createNativeQuery(nativeQuery);
            query.setParameter(1, externalId);
            int numUpdated = query.executeUpdate();

            tx.commit();
        }
        catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    public void updateStatus(String requestId, eRequestStatus status) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        try{
            tx.begin();
            String nativeQuery = "UPDATE notifier.requests set status = '"+status.getDbName()+"' where e_id = ?";

            Query query = entityManager.createNativeQuery(nativeQuery);
            query.setParameter(1, requestId);
            int numUpdated = query.executeUpdate();

            tx.commit();
        }
        catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }
}
