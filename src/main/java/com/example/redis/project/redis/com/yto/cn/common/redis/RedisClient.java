package com.example.redis.project.redis.com.yto.cn.common.redis;

import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;

import java.util.Map;

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
     * 根据策略(policy)存储列表(key-scanKey或者key-value)与散列(scanKey-value) <br>
     * 根据写入方式(push)存储列表数据 <br>
     *
     * @param key 列表Key
     * @param scanKey 散列Key
     * @param value 元素值
     * @param push 写入方式(Mark.LPUSH / Mark.RPUSH)
     * @param policy 策略(Mark.KEY / Makr.VALUE)
     * @return 如果写入成功则返回true， 写入失败返回false
     */
    boolean push(String key, String scanKey, Object value, Mark push, Mark policy) throws Exception;



    /**
     * 返回 key 所关联的字符串值。
     *
     * @param key 散列Key
     * @return 当 key 不存在时，返回 null ，否则，返回 key 的值。如果 key 不是字符串类型，那么返回一个错误。
     */
    String get(String key);


    /**
     * 将字符串值 value 关联到 key 。
     *
     * @param key 散列Key
     * @param value 散列值
     * @return 在 Redis 2.6.12 版本以前， SET 命令总是返回 OK 。<br>
     * 从 Redis 2.6.12 版本开始， SET 在设置操作成功完成时，才返回 OK 。<br>
     * 如果设置了 NX 或者 XX ，但因为条件没达到而造成设置操作未执行，那么命令返回空批量回复（NULL Bulk Reply）。
     *
     */
    String set(String key, String value);

    /**
     * 将字符串值 value 关联到 key, 当且仅当给定 key 不存在时.
     *
     * @param key 散列Key
     * @param value 散列值
     * @return 设置成功，返回 1 。设置失败，返回 0 。
     */
    long setByNX(String key, String value);


    /**
     * 删除给定的一个或多个 key 。
     *
     * @param keys key动态数组
     */
    long del(String... keys);

    /**
     * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     *
     * @param key 哈希表Key
     * @param fields 哈希域动态数组
     * @return 被成功移除的域的数量，不包括被忽略的域。
     */
    long hdel(String key, String... fields);

    /**
     * 将哈希表 key 中的域 field 的值设为 value 。<br>
     * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。<br>
     * 如果域 field 已经存在于哈希表中，旧值将被覆盖。<br>
     *
     * @param key 哈希表Key
     * @param field 哈希域
     * @param value 数据内容
     * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 true 。如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 false 。
     *
     */
    Long hset(String key, String field, String value);


    /**
     * 返回哈希表 key 中给定域 field 的值。
     *
     * @param key 哈希表Key
     * @param field 哈希域
     * @return 给定域的值。当给定域不存在或是给定 key 不存在时，返回 null 。
     */
    String hget(String key, String field);

    /**
     * 返回哈希表 key 中的值。
     * @param key 哈希表Key
     * @return 给定域的值。当给定域不存在或是给定 key 不存在时，返回 null 。
     */
    Map<String, String> hgetAll(String key);



    /**
     * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
     *
     * @param key 元素Key
     * @return 当 key 不存在时，返回 -2 。<br>
     * 当 key 存在但没有设置剩余生存时间时，返回 -1 。<br>
     * 否则，以秒为单位，返回 key 的剩余生存时间。
     *
     */
    long ttl(String key);

    /**
     * 检查给定 key 是否存在。
     *
     * @param key 散列Key
     * @return 如果存在则返回true，否则返回false
     */
    boolean exists(String key);

    /**
     * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
     *
     * @param key 散列Key
     * @param seconds 过期时间，单位：秒
     * @return 设置成功返回 1 。当 key 不存在或者不能为 key 设置生存时间时(比如在低于 2.1.3 版本的 Redis 中你尝试更新 key 的生存时间)，返回 0 。
     */
    long expire(String key, int seconds);


    /**
     * 开放原生态ShardedJedis
     * @param redisType
     * @return
     */
    public RedisClient getRedisClient(String redisType);

    /**
     * 开放原生态ShardedJedis
     * @param redisType
     * @return
     */
    public ShardedJedis getRedis(String redisType);

}
