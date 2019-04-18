package com.example.redis.project.redis.com.yto.cn.common.mq.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.redis.project.redis.com.yto.cn.common.factory.ProducerManager;
import com.example.redis.project.redis.com.yto.cn.common.factory.TopicFactory;
import com.example.redis.project.redis.com.yto.cn.common.mq.MqService;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author: create by YangYK
 * @version: v1.0
 * @date:2019/4/16
 */
public class MqServiceImpl implements MqService {
    private static final Logger logger = LoggerFactory.getLogger(MqServiceImpl.class);

    // 发布数据失败再次尝试前休眠时间
    private static final long SLEEPTIME = 500L;
    // 最多重新发布数据次数
    private static final int TRYTIMES = 5;

    private final DefaultMQProducer producer = ProducerManager.getProducer();

    private final int three = 3;

    private String topic;

    private String getTopic(final String opKind) {
        if (this.topic == null) {
            this.topic = TopicFactory.getTopic(opKind);
        }
        return this.topic;
    }

    @Override
    public List<String> publishing(List<String> list, String opKind) {
        List<String> successList = Lists.<String>newArrayListWithCapacity(list.size());
        long start = System.currentTimeMillis();
        for(String str : list){
            if(StringUtils.isNotEmpty(str)){
                JSONObject jsonObject = JSON.parseObject(str);
                String waybillNo = jsonObject.getString("waybillNo");
                String id = jsonObject.getString("id");
                Message msg = new Message(getTopic(opKind), jsonObject.getString("opCode"), id, str.getBytes(Charsets.UTF_8));
                int temp = 0;
                if (null !=  waybillNo){
                    try {
                        temp = Integer.parseInt(waybillNo.substring(waybillNo.length() - three));
                    }catch (Exception e) {
                        logger.error("单号后三位转int出错:{}", waybillNo);
                    }
                }
                if(publishing(msg, waybillNo, temp, TRYTIMES)){
                    successList.add(str);
                }
            }
        }
        logger.info("发布{}条数据耗时 {}ms", successList.size(), System.currentTimeMillis() - start);
        return successList;
    }

    @Override
    public Map<String, String> publishing(Map<String, String> map, String opKind) {
        return null;
    }


    private boolean publishing(Message msg, String waybillNo, int lastThree, int tryTimes){
        try {
            SendResult sendResult = null;
            if(null == waybillNo){
                sendResult =producer.send(msg);
            } else {
                sendResult = producer.send(msg, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> list, Message message, Object arg) {
                        int id = (int) arg;
                        int index = id % list.size();
                        index = Math.abs(index);// 防止下标负数
                        return list.get(index);
                    }
                },lastThree);
            }
            if(sendResult.getSendStatus() == SendStatus.SEND_OK){
                return true;
            } else {
                logger.error("发布message到mq没有得到成功返回：waybillNo = {}, sendStatus = {}, 等待下一个批次重发", waybillNo, sendResult.getSendStatus());
            }
        } catch (Exception e) {
            logger.error("发布message到mq出错：{}, {}ms 后重试！", e.getMessage(), SLEEPTIME, e);
            try {
                Thread.sleep(SLEEPTIME);
            } catch (Exception e1){
                // Ingore
            }
            if(tryTimes <= 0){
                return false;
            } else {
                publishing(msg, waybillNo, lastThree, tryTimes-1);
            }
        }
        return false;

    }

}
