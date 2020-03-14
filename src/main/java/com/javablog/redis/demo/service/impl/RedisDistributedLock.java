package com.javablog.redis.demo.service.impl;

import com.javablog.redis.demo.service.AbstractDistributedLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.UUID;

@Service("redisDistributedLock")
public class RedisDistributedLock extends AbstractDistributedLock {
    private final Logger logger = LoggerFactory.getLogger(RedisDistributedLock.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private ThreadLocal<String> lockFlag = new ThreadLocal<String>();

    public static final String UNLOCK_LUA;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }


    public RedisDistributedLock() {
        super();
    }

    @Override
    public boolean lock(String key, long expire, int retryTimes, long sleepMillis) {
        boolean result = setRedis(key, expire);
        // 如果获取锁失败，按照传入的重试次数进行重试
        while((!result) && retryTimes-- > 0){
            try {
                logger.debug("lock failed, retrying..." + retryTimes);
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                return false;
            }
            result = setRedis(key , expire);
        }
        return result;
    }

    private boolean setRedis(final String key, final long expire ) {
        try{
            RedisCallback<Boolean> callback = (connection) -> {
                String uuid = UUID.randomUUID().toString();
                lockFlag.set(uuid);
                return connection.set(key.getBytes(Charset.forName("UTF-8")), uuid.getBytes(Charset.forName("UTF-8")),
                        Expiration.milliseconds(expire), RedisStringCommands.SetOption.SET_IF_ABSENT);
            };
            return (Boolean)redisTemplate.execute(callback);
        } catch (Exception e) {
            logger.error("redis lock error.", e);
        }
        return false;
    }


    @Override
    public boolean releaseLock(String key) {
        // 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
        try {
            RedisCallback<Boolean> callback = (connection) -> {
                String value = lockFlag.get();
                return connection.eval(UNLOCK_LUA.getBytes(), ReturnType.BOOLEAN ,1,
                        key.getBytes(Charset.forName("UTF-8")), value.getBytes(Charset.forName("UTF-8")));
            };
            return (Boolean)redisTemplate.execute(callback);
        } catch (Exception e) {
            logger.error("release lock occured an exception", e);
        } finally {
            // 清除掉ThreadLocal中的数据，避免内存溢出
            lockFlag.remove();
        }
        return false;
    }

}


