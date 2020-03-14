package com.javablog.redis.demo.service;

public interface PublishService {
    public void publish(String topicName,String message);
}
