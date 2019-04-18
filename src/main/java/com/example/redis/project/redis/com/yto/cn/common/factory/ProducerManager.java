package com.example.redis.project.redis.com.yto.cn.common.factory;

import com.example.redis.project.redis.com.yto.cn.common.util.LocalConstants;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: create by YangYK
 * @version: v1.0
 * @date:2019/4/16
 */
public class ProducerManager {
    private static final Logger logger = LoggerFactory.getLogger(ProducerManager.class);
    private static final String PRODUCERGROUPNAME = "YYKMQ_PRODUCER";
    private static final String INSTANCENAME = "YYKMQ";
    private  ProducerManager(){}
    private static ThreadLocal<DefaultMQProducer> PRODUCERMANAGER = new ThreadLocal<DefaultMQProducer>(){
        @Override
        protected DefaultMQProducer initialValue() {
            try {
                DefaultMQProducer producer = new DefaultMQProducer(PRODUCERGROUPNAME);
                producer.setNamesrvAddr(LocalConstants.NAMESRVADDR);
                producer.setInstanceName(INSTANCENAME);
                producer.start();
                Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        producer.shutdown();
                    }
                }));
                return producer;
            } catch (Exception e) {
                logger.error("producerManager创建失败, error message {}", e.getMessage());
            }
            return null;
        }
    };
    public static DefaultMQProducer getProducer(){
        return PRODUCERMANAGER.get();
    }

    public static void setProducer(DefaultMQProducer producer){
        PRODUCERMANAGER.set(producer);
    }

}
