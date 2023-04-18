package managers;

import com.example.demo.ConfigurationManager;
import enums.eRedisDB;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;
import java.util.Set;

public class RedisManager {
    private Jedis jedis;
    private static RedisManager instance;

    private static final String LISTENERS_KEY = ConfigurationManager.getInstance().getRedisListenersKey();


    private RedisManager(){
        String redisHost = ConfigurationManager.getInstance().getRedisHost();
        int redisPort = ConfigurationManager.getInstance().getRedisPort();
         jedis = new Jedis(new HostAndPort(redisHost, redisPort));

         jedis.select(eRedisDB.DB_0.getDB());
    }

    public static synchronized RedisManager getInstance(){
        if(instance == null){
            instance = new RedisManager();
        }
        return instance;
    }

    public Set<Map.Entry<String, String>> readAllListeners(){
        try {
            // Read data from Redis key "listeners"
            Map<String, String> listeners = jedis.hgetAll(LISTENERS_KEY);

            return listeners.entrySet();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        finally {
            jedis.close();
        }

    }

    public boolean addListener(String url, String json){
       try{
           jedis.hset(LISTENERS_KEY, url, json);
       }
       catch (Exception ex){
           ex.printStackTrace();
           return false;
       }
       finally {
           // Close the Redis connection
           jedis.close();
       }


        // Close the Redis connection
        jedis.close();
        return true;
    }

    public boolean removeListener(String url){
        try{
            jedis.hdel(LISTENERS_KEY, url);
            return true;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        finally {
            // Close the Redis connection
            jedis.close();
        }
    }

    public boolean removeAllListeners(){
        try {
            jedis.del(LISTENERS_KEY);
            return true;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        finally {
            // Close the Redis connection
            jedis.close();
        }
    }


    public boolean subscribeToChannel(String channel, JedisPubSub listener){
        try{
            new Thread(()->
                jedis.subscribe(listener, channel)).start();
            return true;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        finally {
            // Close the Redis connection
            jedis.close();
        }

    }

    public boolean publishMessageToChannel(String channel, String message){
        try{
            jedis.publish(channel, message);
            return true;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        finally {
            // Close the Redis connection
            jedis.close();
        }

    }

}
