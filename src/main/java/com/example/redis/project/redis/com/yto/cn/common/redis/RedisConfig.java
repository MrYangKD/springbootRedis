package com.example.redis.project.redis.com.yto.cn.common.redis;

import lombok.Data;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("classpath:config/redis.properties")
@Data
public class RedisConfig {
    private List<String> redisType = new ArrayList<>();

    private List<String> hostNames = new ArrayList<>();

    private List<String> poolMaxIdle = new ArrayList<>();

    private List<String> poolMinIdle = new ArrayList<>();

    private List<String> timeout = new ArrayList<>();
}
