package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


public class ConfigurationManager {

    private static ConfigurationManager _instance;

    @Value("${app.redis_port}")
    public Integer redisPort;

    private ConfigurationManager(){
    }

    public static synchronized ConfigurationManager getInstance(){
        if(_instance == null){
            _instance = new ConfigurationManager();
        }
        return _instance;
    }

    public Integer getRedisPort(){
        return redisPort;
    }
}
