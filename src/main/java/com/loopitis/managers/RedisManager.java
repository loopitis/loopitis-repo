package com.loopitis.managers;

import com.loopitis.endpoints.ConfigurationManager;
import com.loopitis.endpoints.LoopitisApplication;
import com.loopitis.endpoints.LoopitisMainEndpoints;
import redis.clients.jedis.*;

import java.util.Map;
import java.util.Set;

public class RedisManager {
    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(LoopitisApplication.MY_LOGGER);
    private static final String LISTENERS_KEY = ConfigurationManager.getInstance().getRedisListenersKey();
    private static final String REDIS_PASSWORD = ConfigurationManager.getInstance().getRedisPassword();
    private static RedisManager instance;
    private JedisPool pool;

    private RedisManager() {
        String redisHost = ConfigurationManager.getInstance().getRedisHost();
        Integer redisPort = ConfigurationManager.getInstance().getRedisPort();

        if (redisPort == null || redisPort < 1) {
            redisPort = 443;
        }
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10);
        config.setMaxIdle(5);
        config.setMinIdle(2);
        log.debug("Connecting to loopitis redis on host " + redisHost + " and port " + redisPort);
        log.debug("Connecting to loopitis redis wit password");

        pool = new JedisPool(config, redisHost, redisPort, Protocol.DEFAULT_TIMEOUT, REDIS_PASSWORD);


    }

    public static void main(String[] args) {
        RedisManager.getInstance().publishMessageToChannel(LoopitisMainEndpoints.REDIS_CANCEL_CHANNEL, "koko");
//        var res= RedisManager.getInstance().subscribeToChannel(TestEndpoint.REDIS_CANCEL_CHANNEL, new JedisPubSub() {
//            @Override
//            public void onMessage(String channel, String message) {
//                System.out.println("koko");
//            }
//        });
//        System.out.println(res);
    }

    public static synchronized RedisManager getInstance() {
        if (instance == null) {
            log.debug("new redis");
            instance = new RedisManager();
        }
        return instance;
    }

    public Set<Map.Entry<String, String>> readAllListeners() {
        try (Jedis jedis = pool.getResource()) {
            // Read data from Redis key "listeners"
            Map<String, String> listeners = jedis.hgetAll(LISTENERS_KEY);

            return listeners.entrySet();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean addListener(String url, String json) {
        try (Jedis jedis = pool.getResource()) {
            jedis.hset(LISTENERS_KEY, url, json);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean removeListener(String url) {
        try (Jedis jedis = pool.getResource()) {
            jedis.hdel(LISTENERS_KEY, url);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public boolean removeAllListeners() {
        try (Jedis jedis = pool.getResource()) {
            jedis.del(LISTENERS_KEY);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }


    public boolean subscribeToChannel(String channel, JedisPubSub listener) {
        try (Jedis jedis = pool.getResource()) {
            jedis.subscribe(listener, channel);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public boolean publishMessageToChannel(String channel, String message) {
        try (Jedis jedis = pool.getResource()) {
            jedis.publish(channel, message);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
