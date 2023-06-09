package com.loopitis.managers;

import com.loopitis.endpoints.ConfigurationManager;
import com.loopitis.endpoints.LoopitisApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.loopitis.general.DBConfiguration;
import com.loopitis.general.DBConfigurationException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.loopitis.consumer.ExecutionRequest;
import com.loopitis.ent.HttpNotifierRequestEntity;
import com.loopitis.enums.eProcess;
import com.loopitis.enums.eRequestStatus;
import com.loopitis.filters.ExecutionsFilter;
import com.loopitis.filters.RequestsFilter;
import com.loopitis.general.CommentRequest;
import com.loopitis.general.DBTestInitManager;
import org.eclipse.persistence.config.PersistenceUnitProperties;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DBManager {
    private static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(LoopitisApplication.MY_LOGGER);

    private static boolean DB_READ_ONLY = false;

    private static DBManager dbManager = null;
    private static int DB_MAX_CONNECTIONS = 1;
    private static String DB_NAME = "mydb";
    private static int DB_PORT_NUMBER = 5432;
    private static String DB_SERVER_HOST = "redis";
    private EntityManagerFactory sessionFactory;
    private String DB_USER = "myUser";
    private String DB_PASSWORD = "mypassword";

    private DBManager() {
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

    public static void main(String[] args) throws JsonProcessingException {
        Gson g = new Gson();
        DBManager manager = DBManager.getInstance();

        RequestsFilter filter = new RequestsFilter();
        filter.withStatus(eRequestStatus.ON_GOING);
        filter.withLimit(20);
//
        HttpNotifierRequestEntity res = manager.getRequests(filter).get(0);
//
        System.out.println(g.toJson(res));


//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonData = objectMapper.readTree(json);
//


//        System.out.println(notifierRequest.getInterval());
//        Person p = new Person("hey", "Jude");
//        p.setId(4);
//        manager.addPerson(p);
//        System.out.println(p);

    }

    public static synchronized DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }

    private void testSelect() {
        String queryString = "SELECT e FROM Employee e WHERE e.department = :dept";
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
        try {
            tx.begin();
            em.persist(request);
            tx.commit();
        } catch (Exception e) {
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
        try {
            tx.begin();
            em.persist(request);
            tx.commit();
        } catch (Exception e) {
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
        try {
            tx.begin();
            String nativeQuery = "UPDATE notifier.executions set status_code = ? where i_id = ?";

            Query query = entityManager.createNativeQuery(nativeQuery);
            query.setParameter(1, exec.getStatusCode());
            query.setParameter(2, exec.getId());
            int numUpdated = query.executeUpdate();

            tx.commit();
        } catch (Exception e) {
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
        try {
            tx.begin();
            String nativeQuery = "UPDATE notifier.requests set done = done+1 where e_id = ?";

            Query query = entityManager.createNativeQuery(nativeQuery);
            query.setParameter(1, externalId);
            int numUpdated = query.executeUpdate();

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    public int updateStatus(String requestId, eRequestStatus status) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            String nativeQuery = "UPDATE notifier.requests set status =? where e_id = ?";

            Query query = entityManager.createNativeQuery(nativeQuery);
            query.setParameter(1, status.getDbName());
            query.setParameter(2, requestId);
            int numUpdated = query.executeUpdate();

            tx.commit();
            return numUpdated;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            return -1;
        } finally {
            entityManager.close();
        }
    }

    public boolean updateCommentOnExecution(CommentRequest commentRequest) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            String nativeQuery = "UPDATE notifier.executions set comment = ? where e_id = ?";

            Query query = entityManager.createNativeQuery(nativeQuery);
            query.setParameter(1, commentRequest.getComment());
            query.setParameter(2, commentRequest.getExecutionId());

            int numUpdated = query.executeUpdate();

            tx.commit();
            if (numUpdated == 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    public List<HttpNotifierRequestEntity> getRequests(RequestsFilter filter) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        try {
            String jpql = "SELECT req FROM HttpNotifierRequestEntity req ";
            if (filter != null) {
                boolean isFirstCondition = true;
                if (filter.getStatus() != null) {
                    if (isFirstCondition) {
                        jpql += "WHERE ";
                        isFirstCondition = false;
                    }
                    jpql += "req.status= :status ";
                }
                if (filter.getRequestId() != null) {
                    if (isFirstCondition) {
                        jpql += "WHERE ";
                        isFirstCondition = false;
                    } else {
                        jpql += " and ";
                    }
                    jpql += "req.externalId= :requestId ";
                }

            }
            TypedQuery<HttpNotifierRequestEntity> query = entityManager.createQuery(jpql, HttpNotifierRequestEntity.class);
            if (filter != null) {
                if (filter.getStatus() != null) {
                    query.setParameter("status", filter.getStatus());
                }
                if (filter.getRequestId() != null) {
                    query.setParameter("requestId", filter.getRequestId());
                }
                if (filter.getLimit() != null) {
                    query.setMaxResults(filter.getLimit());
                }
            }
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    public void resetQueryCache() {
        EntityManager entityManager = sessionFactory.createEntityManager();
        try {

            String nativeQuery = "RESET QUERY CACHE";
            Query query = entityManager.createNativeQuery(nativeQuery);

            query.executeUpdate();

        } finally {
            entityManager.close();
        }
    }

    public List<ExecutionRequest> getExecutions(ExecutionsFilter filter) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        try {
            String jpql = "SELECT req FROM ExecutionRequest req ";
            if (filter != null) {
                if (filter.getRequestId() != null) {
                    jpql += " WHERE req.requestId= :req ";
                }
                if (filter.getComment() != null) {
                    jpql += "and req.comment= :comment ";
                }

                jpql += "order by req.timeExecuted ";

            }
            TypedQuery<ExecutionRequest> query = entityManager.createQuery(jpql, ExecutionRequest.class);
            if (filter != null) {
                if (filter.getRequestId() != null) {
                    query.setParameter("req", filter.getRequestId());
                }
                if (filter.getComment() != null) {
                    query.setParameter("comment", filter.getComment());
                }
                if (filter.getLimit() != null) {
                    query.setMaxResults(filter.getLimit());
                }
            }
            return query.getResultList();

        } finally {
            entityManager.close();
        }
    }
}
