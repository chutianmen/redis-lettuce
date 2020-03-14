package com.javablog.redis.demo.service.impl;

import com.javablog.redis.demo.service.HashCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service("hashCacheService")
public class HashCacheServiceImpl implements HashCacheService {
    private final static Logger log = LoggerFactory.getLogger(HashCacheServiceImpl.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取MAP中的某个值
     * @param key  键
     * @param item 项
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public Map <Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 以map集合的形式添加键值对
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map <String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map <String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public long hincr(String key, String item, long by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public long hdecr(String key, String item, long by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * 获取指定变量中的hashMap值。
     * @param key
     * @return 返回LIST对象
     */
    @Override
    public List<Object> values(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    /**
     *  获取变量中的键。
     * @param key
     * @return 返回SET集合
     */
    @Override
    public Set<Object> keys(String key) {
        return  redisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取变量的长度。
     * @param key 键
     * @return  返回长度
     */
    @Override
    public long size(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * 以集合的方式获取变量中的值。
     * @param key
     * @param list
     * @return 返回LIST集合值
     */
    @Override
    public List multiGet(String key, List list) {
        return redisTemplate.opsForHash().multiGet(key,list);
    }

    /**
     *  如果变量值存在，在变量中可以添加不存在的的键值对
     *  如果变量不存在，则新增一个变量，同时将键值对添加到该变量。
     * @param key
     * @param hashKey
     * @param value
     */
    @Override
    public void putIfAbsent(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().putIfAbsent(key,hashKey,value);
    }

    /**
     * 匹配获取键值对，ScanOptions.NONE为获取全部键对，ScanOptions.scanOptions().match("map1").build()
     * 匹配获取键位map1的键值对,不能模糊匹配。
     * @param key
     * @param options
     * @return
     */
    @Override
    public Cursor<Map.Entry<Object, Object>> scan(String key, ScanOptions options) {
        return  redisTemplate.opsForHash().scan(key,options);
    }

    /**
     * 删除变量中的键值对，可以传入多个参数，删除多个键值对。
     * @param key 键
     * @param hashKeys MAP中的KEY
     */
    @Override
    public void delete(String key, String... hashKeys) {
        redisTemplate.opsForHash().delete(key,hashKeys);
    }

    public boolean expire(String key, long seconds) {
        return redisTemplate.expire(key,seconds,TimeUnit.SECONDS);
    }

    /**
     * 删除
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

    @Override
    public long getExpire(String key) {
        return 0;
    }
}



