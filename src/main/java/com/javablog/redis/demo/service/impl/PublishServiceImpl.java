package com.javablog.redis.demo.service.impl;

import com.javablog.redis.demo.service.PublishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service("publishService")
public class PublishServiceImpl implements PublishService {
    private final static Logger log = LoggerFactory.getLogger(PublishServiceImpl.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 订阅主题
     * @param topicName 发布的管道
     * @param message   发布的内容
     */
    @Override
    public void publish(String topicName,String message) {
        stringRedisTemplate.convertAndSend(topicName, message);
    }

}
