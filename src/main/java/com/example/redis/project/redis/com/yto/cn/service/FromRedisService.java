package com.example.redis.project.redis.com.yto.cn.service;

import java.util.List;
import java.util.Map;

public interface FromRedisService {
    String getFromRedis(String key) throws Exception;
    String setRedis(String key, String value) throws  Exception;
    List<String> getInfoFromData(String waybillNo) throws Exception;

}
