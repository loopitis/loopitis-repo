package managers;

import com.example.demo.ConfigurationManager;
import enums.eRedisDB;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisManager {
    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(RedisManager.class);

    private static RedisManager _instance;


    public static synchronized RedisManager getInstance(){
        if(_instance == null){
            _instance = new RedisManager();
        }
        return _instance;
    }

    private JedisPool jedisPool;

    public static void main(String[] s) {
        RedisManager manager = RedisManager.getInstance();


        manager.addValueToList("kok", "bla", eRedisDB.DB_0);

    }


    private RedisManager () {
        if(jedisPool == null) {
            String host = "localhost";
//            String pass = ConfigurationManager.getInstance().getExternalRedisPass();
            int port = ConfigurationManager.getInstance().getRedisPort();
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxIdle(10);
            poolConfig.setMinIdle(10);
            poolConfig.setMaxWaitMillis(20000);
            int db = 1;
            jedisPool = new JedisPool(poolConfig, host, port, 1000, null, db);
        }
    }

        public void addValueToList(String keyListName, String json, eRedisDB rdisDb) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(rdisDb.getDB());
            long t = jedis.lpush(keyListName, json);

            // Specific commands
            jedis.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            //In JedisPool mode, the Jedis resource will be returned to the resource pool.
            if (jedis != null)
                jedis.close();
        }
    }
//
//    public void addValueToSet(String keyListName, eRedisDB rdisDb, String ... value) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            jedis.select(rdisDb.getDB());
//            jedis.sadd(keyListName, value);
//            // Specific commands
//            jedis.close();
//        }
//        catch (Exception e) {
//            log.error("Failed adding values to Redis set!");
//            SoftwareIssuesManager.getInstance().notifyFB("Failed inserting values to Redis ! Check that Redis recogniz your ip. some products might not be displaying in the alerts section");
//        }
//        finally {
//            //In JedisPool mode, the Jedis resource will be returned to the resource pool.
//            if (jedis != null)
//                jedis.close();
//        }
//    }
//
//
//
//    public void increaseCounter(String key, String hash, int increaseBy, eRedisDB rdisDb) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            jedis.select(rdisDb.getDB());
//            jedis.hincrBy(key, hash, increaseBy);
//            // Specific commands
//            jedis.close();
//        }
//        catch (Exception e) {
//        }
//        finally {
//            //In JedisPool mode, the Jedis resource will be returned to the resource pool.
//            if (jedis != null)
//                jedis.close();
//        }
//
//    }
//
//
//
//
//    public void replaceValueInSetWithTTL(String sellerId, String token, long seconds, eRedisDB rdisDb) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            jedis.select(rdisDb.getDB());
//            jedis.del(sellerId);
//            jedis.sadd(sellerId, token);
//            jedis.expire(sellerId, (int)seconds);
//
//            // Specific commands
//            jedis.close();
//        }
//        catch (Exception e) {
//        }
//        finally {
//            //In JedisPool mode, the Jedis resource will be returned to the resource pool.
//            if (jedis != null)
//                jedis.close();
//        }
//    }
//
//    public String getValueFromSet(String sellerId, eRedisDB rdisDb) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            jedis.select(rdisDb.getDB());
//            Set<String> names=jedis.smembers(sellerId);
//            if(names == null || names.size() != 1) return null;
//            return names.iterator().next();
//        }
//        catch (Exception e) {
//            log.error("Failed getting value for key : seller: "+sellerId, e);
//            return null;
//        }
//        finally {
//            //In JedisPool mode, the Jedis resource will be returned to the resource pool.
//            if (jedis != null)
//                jedis.close();
//        }
//    }
//
//    public Set<String> getValuesStartWith(String startsWith, eRedisDB redisDb) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            jedis.select(redisDb.getDB());
//            return jedis.keys(startsWith+"*");
////
////		    jedis.expire(sellerId, (int)seconds);
//
//            // Specific commands
//        }
//        catch (Exception e) {
//            log.error("Failed inserting message from FB to local redis", e);
//            return null;
//        }
//        finally {
//            //In JedisPool mode, the Jedis resource will be returned to the resource pool.
//            if (jedis != null)
//                jedis.close();
//        }
//    }
//    public void removeKeysAndValues(Collection<String> keys, eRedisDB redisDb) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            jedis.select(redisDb.getDB());
//            jedis.del(keys.toArray(new String[keys.size()]));
////		    jedis.sadd(sellerId, token);
////
////		    jedis.expire(sellerId, (int)seconds);
//
//            // Specific commands
//            jedis.close();
//        }
//        catch (Exception e) {
//            log.error("Failed inserting message from FB to local redis", e);
//        }
//        finally {
//            //In JedisPool mode, the Jedis resource will be returned to the resource pool.
//            if (jedis != null)
//                jedis.close();
//        }
//    }
//
//    public Long removeSetValue(eRedisDB redisDb, String key, String... value) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            jedis.select(redisDb.getDB());
//            Long removed = jedis.srem(key, value);
////		    jedis.sadd(sellerId, token);
////
////		    jedis.expire(sellerId, (int)seconds);
//
//            // Specific commands
//            jedis.close();
//            return removed;
//        }
//        catch (Exception e) {
//            log.error("Failed inserting message from FB to local redis", e);
//        }
//        finally {
//            //In JedisPool mode, the Jedis resource will be returned to the resource pool.
//            if (jedis != null)
//                jedis.close();
//        }
//        return 0L;
//    }
//
//    public List<String> syngetValuesForKeys(Collection<String> keys, eRedisDB redisDB) {
//        if(keys == null || keys.isEmpty()) return null;
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            jedis.select(redisDB.getDB());
//            List<String> values = jedis.mget(keys.toArray(new String[keys.size()]));
//
//
//            // Specific commands
//            jedis.close();
//            return values;
//        }
//        catch (Exception e) {
//            log.error("Failed inserting message from FB to local redis", e);
//            return null;
//        }
//        finally {
//            //In JedisPool mode, the Jedis resource will be returned to the resource pool.
//            if (jedis != null)
//                jedis.close();
//        }
//    }
//
//    public void synAddkeysValuesToMset(HashMap<String, String> keyVal, eRedisDB redisDB) {
//        List<String> keyValuelist = new ArrayList<String>();
//        for(Map.Entry<String,String> keyValEntry : keyVal.entrySet()) {
//            keyValuelist.add(keyValEntry.getKey());
//            keyValuelist.add(keyValEntry.getValue());
//
//        }
//
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            jedis.select(redisDB.getDB());
//            jedis.mset(keyValuelist.toArray(new String[keyValuelist.size()]));
//
//            // Specific commands
//            jedis.close();
//        }
//        catch (Exception e) {
//            log.error("Failed inserting message from FB to local redis", e);
//        }
//        finally {
//            //In JedisPool mode, the Jedis resource will be returned to the resource pool.
//            if (jedis != null)
//                jedis.close();
//        }
//    }
//
//    public void synAddkeysValuesToMset(String key, HashMap<String, String> keyVal, eRedisDB redisDB) {
//
//
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            jedis.select(redisDB.getDB());
//            jedis.hmset(key, keyVal);
//
//            // Specific commands
//            jedis.close();
//        }
//        catch (Exception e) {
//            log.error("Failed inserting message from FB to local redis", e);
//        }
//        finally {
//            //In JedisPool mode, the Jedis resource will be returned to the resource pool.
//            if (jedis != null)
//                jedis.close();
//        }
//    }
//
//    public void synRemoveValuesFromMset(eRedisDB redisDB, String hashKey, String key) {
//
//
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            jedis.select(redisDB.getDB());
//            jedis.hdel(hashKey, key);
//
//            // Specific commands
//            jedis.close();
//        }
//        catch (Exception e) {
//            log.error("Failed inserting message from FB to local redis", e);
//        }
//        finally {
//            //In JedisPool mode, the Jedis resource will be returned to the resource pool.
//            if (jedis != null)
//                jedis.close();
//        }
//    }
//
//    public Map<String, String> getHashForKey(String key, eRedisDB redisDB) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            jedis.select(redisDB.getDB());
//            Map<String, String>  result = jedis.hgetAll(key);
//
//            // Specific commands
//            jedis.close();
//            return result;
//        }
//        catch (Exception e) {
//            log.error("Failed inserting message from FB to local redis", e);
//            return null;
//        }
//        finally {
//            //In JedisPool mode, the Jedis resource will be returned to the resource pool.
//            if (jedis != null)
//                jedis.close();
//        }
//    }

}
