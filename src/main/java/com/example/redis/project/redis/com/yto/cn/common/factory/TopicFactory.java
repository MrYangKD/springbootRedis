package com.example.redis.project.redis.com.yto.cn.common.factory;

import com.example.redis.project.redis.com.yto.cn.common.util.LocalConstants;

/**
 * @author: create by YangYK
 * @version: v1.0
 * @date:2019/4/16
 */
public class TopicFactory {
    private TopicFactory(){
    }

    /**
     * @param opKind
     * @return
     */
    public static String getTopic(String opKind) {
        return LocalConstants.TOPIC.get(opKind);
    }
}
