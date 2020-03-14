package com.javablog.redis.demo.service.impl;

import com.javablog.redis.demo.service.KeyValueCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service("keyValueCacheService")
public class KeyValueCacheServiceImpl implements KeyValueCacheService {
    private final static Logger log = LoggerFactory.getLogger(KeyValueCacheServiceImpl.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @param expireTime 超时时间(秒)
     * @return true成功 false失败
     */
    @Override
    public Boolean set(String key, Object value, int expireTime) {
        try {
            redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @param expireTime 超时时间(秒)
     * @return true成功 false失败
     */
    @Override
    public Boolean set(String key, String value, int expireTime) {
        try {
            stringRedisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    @Override
    public Boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    @Override
    public Boolean set(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    @Override
    public String get(String key) {
        return key == null ? null : (String) stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 在原有的值基础上新增字符串到末尾。
     * @param key 键
     * @param value 追加的值
     * @return true成功 false失败
     */
    @Override
    public void append(String key, String value) {
        stringRedisTemplate.opsForValue().append(key,value);
    }

    /**
     * 截取key键对应值得字符串，从开始下标位置开始到结束下标的位置(包含结束下标)的字符串。
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return
     */
    @Override
    public String get(String key, long start, long end) {
        return stringRedisTemplate.opsForValue().get(key,start,end);
    }

    /**
     * 获取原来key键对应的值并重新赋新值。
     * @param key 键
     * @param value 值
     * @return 原来旧值
     */
    @Override
    public Object getAndSet(String key, String value) {
        return stringRedisTemplate.opsForValue().getAndSet(key,value);
    }

    /**
     * 覆盖从指定位置开始的值。
     * @param key  键
     * @param value  值
     * @param offset  位置
     */
    @Override
    public void set(String key, String value, long offset) {
        redisTemplate.opsForValue().set(key,value,offset);
    }

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    @Override
    public Object getObject(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     *  获取指定字符串的长度。
     * @param key
     * @return 长度
     */
    @Override
    public long size(String key) {
        return stringRedisTemplate.opsForValue().size(key);
    }

    /**
     *  如果键不存在则新增,存在则不改变已经有的值
     * @param key
     * @param value
     * @return true成功 false失败
     */
    @Override
    public boolean setIfAbsent(String key, String value){
        return stringRedisTemplate.opsForValue().setIfAbsent(key,value);
    }

    /**
     * 删除指定KEY的缓存
     * @param keys
     */
    @Override
    public void del(String... keys) {
        if (keys != null && keys.length > 0) {
            if (keys.length == 1) {
                redisTemplate.delete(keys[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(keys));
            }
        }
    }

    /**
     * 根据key设置过期时间
     * @param key 键
     * @param seconds 超时时间(秒)
     * @return true成功 false失败
     */
    @Override
    public boolean expire(String key, long seconds) {
        return redisTemplate.expire(key,seconds,TimeUnit.SECONDS);
    }

    /**
     * 根据key获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    @Override
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 递增
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return 返回增加后的值
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return 返回减少后的值
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return stringRedisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * 设置map集合到redis。
     * @param map
     */
    @Override
    public void multiSet(Map map) {
        stringRedisTemplate.opsForValue().multiSet(map);
    }

    /**
     * 根据集合取出对应的value值。
     * @param list
     * @return
     */
    @Override
    public List multiGet(List list) {
        return stringRedisTemplate.opsForValue().multiGet(list);
    }
}



