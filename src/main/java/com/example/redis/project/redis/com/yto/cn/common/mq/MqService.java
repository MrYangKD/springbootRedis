package com.example.redis.project.redis.com.yto.cn.common.mq;

import com.example.redis.project.redis.com.yto.cn.common.mq.impl.MqServiceImpl;
import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.Map;

/**
 * @author: create by YangYK
 * @version: v1.0
 * @date:2019/4/16
 */
@ImplementedBy(MqServiceImpl.class)
public interface MqService {

    /**
     * 发布信息,list.
     * @param list 数据类型
     * @param opKind opKind
     * @return 成功发布的数据
     */
    List<String> publishing(List<String> list, String opKind);

    /**
     * 发布信息,map.
     * @param map 数据类型
     * @param opKind opKind
     * @return 成功发布的数据
     */
    Map<String, String> publishing(Map<String, String> map, String opKind);
}
