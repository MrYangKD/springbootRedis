package com.example.redis.project.redis.com.yto.cn.common.redis;

import org.springframework.stereotype.Component;

@Component
public interface RedisClient {
    enum Mark {
        /** 从列表的左端读取元素 */
        LPOP,

        /** 从列表的右端读取元素 */
        RPOP,

        /** 将元素写入列表的左端 */
        LPUSH,

        /** 将元素写入列表的右端 */
        RPUSH,

        /** linsert position on before */
        BEFORE,

        /** linsert position on after */
        AFTER,

        /** PUSH列表时的策略，以KEY为基准 */
        KEY,

        /** PUSH列表时的策略，以VALUE为基准 */
        VALUE;
    }

    /**
     * 开放原生态ShardedJedis
     * @param redisType
     * @return
     */
    public RedisClient getRedisClient(String redisType);

}
