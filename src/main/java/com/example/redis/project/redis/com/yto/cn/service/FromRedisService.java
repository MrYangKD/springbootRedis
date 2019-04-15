package com.example.redis.project.redis.com.yto.cn.service;

import java.util.Map;

public interface FromRedisService {
    String getFromRedis(String key) throws Exception;
    String setRedis(String key, String value) throws  Exception;
}
