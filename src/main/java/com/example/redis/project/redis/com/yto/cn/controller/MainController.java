package com.example.redis.project.redis.com.yto.cn.controller;

import com.example.redis.project.redis.com.yto.cn.service.FromRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/index")
@ResponseBody
public class MainController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private FromRedisService tmpService;

    @RequestMapping("/contro")
    private String getIndex(@RequestParam String a ) throws Exception {
        logger.info("begin");
       String result =  tmpService.setRedis(a,"yongkang");
       String _result = tmpService.getFromRedis(a);
       return _result;
    }
}
