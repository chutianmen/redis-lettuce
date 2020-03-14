package com.javablog.redis.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageReceiver {
    private final static Logger log = LoggerFactory.getLogger(MessageReceiver.class);
    public void receiveMessage(String message) {
        log.info("Received >>>>>>>>>>>>>>>>>>>>>>:" + message );
    }
}
