package com.example.redis.project.redis.com.yto.cn.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.MurmurHash;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfigLoad {
    public static final Hashing MURMUR_HASH = new MurmurHash();
    @Autowired
    private RedisConfig redisConfig;

    public static Map<String, RedisClientImpl> globalsRedis = new HashMap();
    private static final Boolean DEFAULT_TEST_ON_BORROW = Boolean.FALSE;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Bean
    public JedisConnectionFactory redisConnectionFactory()  throws Exception {
        loadRedis();
        return new JedisConnectionFactory();
    }
    private void loadRedis(){
        if(redisConfig.getRedisType().size() > 0){
            for (int i = 0; i < redisConfig.getHostNames().size(); i++) {
                JedisPoolConfig jedisPoolConfig = getJedisPoolConfig(redisConfig.getPoolMaxIdle().get(i), redisConfig.getPoolMinIdle().get(i));
                ShardedJedisPool shardedJedisPool = createJedisPool(redisConfig.getHostNames().get(i), redisConfig.getTimeout().get(i), jedisPoolConfig);
                globalsRedis.put(redisConfig.getRedisType().get(i), new RedisClientImpl(shardedJedisPool));
            }
        }
    }

    private JedisPoolConfig getJedisPoolConfig(String maxTotal, String maxIdle) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(Integer.valueOf(maxTotal));
        poolConfig.setMaxIdle(Integer.valueOf(maxIdle));
        poolConfig.setTestOnBorrow(DEFAULT_TEST_ON_BORROW);
        return poolConfig;
    }

    private ShardedJedisPool createJedisPool(String clusterNodes, String timeout, JedisPoolConfig jedisPoolConfig) {
        try {
            String[] hostAndports = clusterNodes.split(";");

            List shards = new ArrayList();
            for (int i = 0; i < hostAndports.length; i++) {
                String[] hostPort = hostAndports[i].split(":");
                shards.add( new JedisShardInfo(hostPort[0], Integer.valueOf(hostPort[1]), Integer.valueOf(timeout)));
            }
            return new ShardedJedisPool(jedisPoolConfig, shards, MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
        } catch (Exception e) {
            logger.error("初始化redis异常" +  e.getMessage());
        }
        return null;
    }
}
