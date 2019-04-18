package com.example.redis.project.redis.com.yto.cn.common.redis;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@PropertySource("classpath:config/redis.properties")
@ConfigurationProperties(prefix="redis")
public class RedisConfig {

    private List<String> redisType = new ArrayList<>();

    private List<String> hostNames = new ArrayList<>();

    private List<String> poolMaxIdle = new ArrayList<>();

    private List<String> poolMinIdle = new ArrayList<>();

    private List<String> timeout = new ArrayList<>();

    public List<String> getRedisType() {
        return redisType;
    }

    public void setRedisType(List<String> redisType) {
        this.redisType = redisType;
    }

    public List<String> getHostNames() {
        return hostNames;
    }

    public void setHostNames(List<String> hostNames) {
        this.hostNames = hostNames;
    }

    public List<String> getPoolMaxIdle() {
        return poolMaxIdle;
    }

    public void setPoolMaxIdle(List<String> poolMaxIdle) {
        this.poolMaxIdle = poolMaxIdle;
    }

    public List<String> getPoolMinIdle() {
        return poolMinIdle;
    }

    public void setPoolMinIdle(List<String> poolMinIdle) {
        this.poolMinIdle = poolMinIdle;
    }

    public List<String> getTimeout() {
        return timeout;
    }

    public void setTimeout(List<String> timeout) {
        this.timeout = timeout;
    }
}
