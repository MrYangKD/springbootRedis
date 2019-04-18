package com.example.redis.project.redis.com.yto.cn.common.util;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author: create by YangYK
 * @version: v1.0
 * @date:2019/4/16
 */
public class LocalConstants {

    /** topic map.*/
    public static final Map<String, String> TOPIC = Maps.newConcurrentMap();

    /** mq集群地址. */
    public static final String NAMESRVADDR = System.getProperty("project.mq.namesrvaddr");
    /** init topic.*/
    static {
        TOPIC.put(OpKind.YYKTaking,System.getProperty("mq.topic.yyk.taking"));
    }

    /** 类型.*/
    public static final class OpKind {
        public  static final String YYKTaking = "YYKTaking";
    }
}
