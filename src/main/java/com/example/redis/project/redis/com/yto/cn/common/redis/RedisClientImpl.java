package com.example.redis.project.redis.com.yto.cn.common.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Response;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

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
    public boolean push(String key, String scanKey, Object value, Mark push, Mark policy) throws Exception {

        ShardedJedis jedis = null;

        try {
            jedis = shardedJedisPool.getResource();
            ShardedJedisPipeline pipeline = jedis.pipelined();

            switch (push) {
                case LPUSH:
                    switch (policy) {
                        case KEY:
                            pipeline.lpush(key, scanKey);
                            break;
                        case VALUE:
                            pipeline.lpush(key, JSON.toJSONString(value));
                            break;
                        default:
                            throw new Exception("未知的策略(policy)类型");
                    }

                    break;
                case RPUSH:
                    switch (policy) {
                        case KEY:
                            pipeline.rpush(key, scanKey);
                            break;
                        case VALUE:
                            pipeline.rpush(key, JSON.toJSONString(value));
                            break;
                        default:
                            throw new Exception("未知的策略(policy)类型");
                    }

                    break;
                default:
                    throw new Exception("未知的写入(PUSH)类型");
            }

            Response<String> okResponse = pipeline.set(scanKey, JSON.toJSONString(value));
            pipeline.sync();

            return isOK(okResponse.get());
        } catch (Exception e) {
            throw e;
        } finally {
            jedis.close();
        }
    }


    @Override
    public String get(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            throw e;
        } finally {
            jedis.close();
        }
    }

    @Override
    public String set(String key, String value) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.set(key, value);
        } catch (Exception e) {
            throw e;
        } finally {
            jedis.close();
        }
    }

    @Override
    public long setByNX(String key, String value) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.setnx(key, value);
        } catch (Exception e) {
            throw e;
        } finally {
            jedis.close();
        }
    }

    @Override
    public long del(String... keys) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            ShardedJedisPipeline pipeline = jedis.pipelined();
            List<Response<Long>> responses = new ArrayList<>();
            for (String key : keys) {
                responses.add(pipeline.del(key));
            }
            pipeline.sync();
            AtomicLong dels = new AtomicLong(0);
            if (!CollectionUtils.isEmpty(responses)) {
                responses.forEach(res -> dels.addAndGet(res.get()));
            }
            return dels.get();
        } catch (Exception e) {
            throw e;
        } finally {
            jedis.close();
        }
    }

    @Override
    public long hdel(String key, String... fields) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            ShardedJedisPipeline pipeline = jedis.pipelined();
            List<Response<Long>> responses = new ArrayList<>();
            for (String field : fields) {
                responses.add(pipeline.hdel(key, field));
            }
            pipeline.sync();
            AtomicLong dels = new AtomicLong(0);
            if (!CollectionUtils.isEmpty(responses)) {
                responses.forEach(res -> dels.addAndGet(res.get()));
            }
            return dels.get();
        } catch (Exception e) {
            throw e;
        } finally {
            jedis.close();
        }
    }

    @Override
    public Long hset(String key, String field, String value) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.hset(key, field, value);
        } catch (Exception e) {
            throw e;
        } finally {
            jedis.close();
        }
    }

    @Override
    public String hget(String key, String field) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.hget(key, field);
        } catch (Exception e) {
            throw e;
        } finally {
            jedis.close();
        }
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.hgetAll(key);
        } catch (Exception e) {
            throw e;
        } finally {
            jedis.close();
        }
    }

    @Override
    public long ttl(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.ttl(key);
        } catch (Exception e) {
            throw e;
        } finally {
            jedis.close();
        }
    }

    @Override
    public boolean exists(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.exists(key);
        } catch (Exception e) {
            throw e;
        } finally {
            jedis.close();
        }
    }

    @Override
    public long expire(String key, int seconds) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.expire(key, seconds);
        } catch (Exception e) {
            throw e;
        } finally {
            jedis.close();
        }
    }

    @Override
    public RedisClient getRedisClient(String redisType) {
        return RedisConfigLoad.globalsRedis.get(redisType);
    }

    protected boolean isOK(String value) {
        return OK.equals(value);
    }

    @Override
    public ShardedJedis getRedis(String redisType) {
        RedisClientImpl redisClient = RedisConfigLoad.globalsRedis.get(redisType);
        return redisClient.shardedJedisPool.getResource();
    }

}
