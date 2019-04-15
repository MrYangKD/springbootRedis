package com.example.redis.project.redis.com.yto.cn.common.redis;

import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedisPool;

@Component
public class RedisClientImpl implements RedisClient {
    final String OK = "OK";
    public ShardedJedisPool shardedJedisPool = null;
    public RedisClientImpl(ShardedJedisPool jedisPool){
        shardedJedisPool = jedisPool;
    }

    public RedisClientImpl() {
    }

    @Override
    public RedisClient getRedisClient(String redisType) {
        return RedisConfigLoad.globalsRedis.get(redisType);
    }
}
