package managers;

import com.example.demo.ConfigurationManager;
import enums.eRedisDB;
import redis.clients.jedis.*;

import java.util.Map;
import java.util.Set;

public class RedisManager {
    private JedisPool pool;
    private static RedisManager instance;

    private static final String LISTENERS_KEY = ConfigurationManager.getInstance().getRedisListenersKey();


    private RedisManager(){
        String redisHost = ConfigurationManager.getInstance().getRedisHost();
        Integer redisPort = ConfigurationManager.getInstance().getRedisPort();

        if(redisPort == null || redisPort < 1){
            redisPort = 443;
        }
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10);
        config.setMaxIdle(5);
        config.setMinIdle(2);
        if(redisPort == null)
            pool = new JedisPool(config, redisHost);
        else pool = new JedisPool(config, redisHost, redisPort);


    }

    public static synchronized RedisManager getInstance(){
        if(instance == null){
            instance = new RedisManager();
        }
        return instance;
    }

    public Set<Map.Entry<String, String>> readAllListeners(){
        try (Jedis jedis = pool.getResource()) {
            // Read data from Redis key "listeners"
            Map<String, String> listeners = jedis.hgetAll(LISTENERS_KEY);

            return listeners.entrySet();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public boolean addListener(String url, String json){
       try (Jedis jedis = pool.getResource()) {
           jedis.hset(LISTENERS_KEY, url, json);
           return true;
       }
       catch (Exception ex){
           ex.printStackTrace();
           return false;
       }
    }

    public boolean removeListener(String url){
        try (Jedis jedis = pool.getResource()) {
            jedis.hdel(LISTENERS_KEY, url);
            return true;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }

    }

    public boolean removeAllListeners(){
        try (Jedis jedis = pool.getResource()) {
            jedis.del(LISTENERS_KEY);
            return true;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }

    }


    public boolean subscribeToChannel(String channel, JedisPubSub listener){
        try (Jedis jedis = pool.getResource()) {
            jedis.subscribe(listener, channel);
            return true;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }

    }

    public boolean publishMessageToChannel(String channel, String message){
        try (Jedis jedis = pool.getResource()) {
            jedis.publish(channel, message);
            return true;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

}
