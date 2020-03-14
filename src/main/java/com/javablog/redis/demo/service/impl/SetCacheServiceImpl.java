package com.javablog.redis.demo.service.impl;

import com.javablog.redis.demo.service.SetCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service("setCacheService")
public class SetCacheServiceImpl implements SetCacheService {
    private final static Logger log = LoggerFactory.getLogger(SetCacheServiceImpl.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     *  向变量中批量添加值。
     * @param key 键
     * @param objects 值
     * @return true成功 false失败
     */
    public boolean add(String key, Object...objects){
        try {
            redisTemplate.opsForSet().add(key,objects);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *  向变量中批量添加值。
     * @param key 键
     * @param expireTime 值
     * @param values 值
     * @return true成功 false失败
     */
    @Override
    public Boolean add(String key, int expireTime, Object... values) {
        try {
            redisTemplate.opsForSet().add(key,values);
            if (expireTime > 0)
                expire(key, expireTime);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * members(K key)获取变量中的值。
     * @param key 键
     * @return 返回Set对象
     */
    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 获取变量中值的长度。
     * @param key 键
     * @return 返回SET的长度
     */
    @Override
    public long size(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 检查给定的元素是否在变量中。
     * @param key 键
     * @param o 要检查的变量
     * @return true存在 false不存在
     */
    @Override
    public boolean isMember(String key, Object o) {
        return redisTemplate.opsForSet().isMember(key,o);
    }

    /**
     * 转移变量的元素值到目的变量。
     * @param key 键
     * @param value 要转移的元素
     * @param destValue 目标键
     * @return true 成功 false 失败
     */
    @Override
    public boolean move(String key, Object value, String destValue) {
        return redisTemplate.opsForSet().move(key,value,destValue);
    }

    /**
     * 弹出变量中的元素。
     * @param key 键
     * @return 返回弹出的元素
     */
    @Override
    public Object pop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 批量移除变量中的元素。
     * @param key 键
     * @param values 要移除的元素
     * @return 返回移除元素个数
     */
    @Override
    public long remove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key,values);
    }

    /**
     * 匹配获取键值对
     * @param key 键
     * @param options 选项
     * @return 返回键值对
     */
    @Override
    public Cursor<Object> scan(String key, ScanOptions options) {
        return redisTemplate.opsForSet().scan(key,options);
    }

    /**
     * 通过集合求差值。
     * @param key 键
     * @param list LIST中的对象是要比较缓存的KEY
     * @return 返回差差值
     */
    @Override
    public Set<Object> difference(String key, List list) {
        return redisTemplate.opsForSet().difference(key,list);
    }

    /**
     * 通过给定的key求2个set变量的差值。
     * @param key 键
     * @param otherKeys 比较的键
     * @return 差值SET
     */
    @Override
    public Set<Object> difference(String key, String otherKeys) {
        return  redisTemplate.opsForSet().difference(key,otherKeys);
    }

    /**
     * 将求出来的差值元素保存。
     * @param key 键
     * @param otherKey 要比较的缓存键
     * @param destKey 要保存差值的缓存键
     */
    @Override
    public void differenceAndStore(String key, String otherKey, String destKey) {
        redisTemplate.opsForSet().differenceAndStore(key,otherKey,destKey);
    }

    /**
     * 将求出来的差值元素保存。
     * @param key 键
     * @param otherKeys 要比较的多个缓存键
     * @param destKey 要保存差值的缓存键
     */
    @Override
    public void differenceAndStore(String key, List otherKeys, String destKey) {
        redisTemplate.opsForSet().differenceAndStore(key,otherKeys,destKey);
    }

    /**
     * 获取去重的随机元素。
     * @param key 键
     * @param count 数量
     * @return 返回随机元素
     */
    @Override
    public Set<Object> distinctRandomMembers(String key, long count) {
        return redisTemplate.opsForSet().distinctRandomMembers(key,count);
    }

    /**
     * 获取2个变量中的交集。
     * @param key 键
     * @param otherKey 比较的缓存键
     * @return 返回交集
     */
    @Override
    public Set<Object> intersect(String key, String otherKey) {
        return redisTemplate.opsForSet().intersect(key,otherKey);
    }

    @Override
    public Set<Object> intersect(String key, List list) {
        return redisTemplate.opsForSet().intersect(key,list);
    }

    /**
     * 获取2个变量交集后保存到最后一个参数上
     * @param key 键
     * @param otherKey 其它的缓存键
     * @param destKey 交集键
     */
    @Override
    public void intersectAndStore(String key, String otherKey, String destKey) {
        redisTemplate.opsForSet().intersectAndStore(key, otherKey, destKey);
    }


    /**
     * 获取2个变量交集后保存到最后一个参数上
     * @param key 键
     * @param otherKey 其它的缓存键列表
     * @param destKey 交集键
     */
    @Override
    public void intersectAndStore(String key, List otherKey, String destKey) {
        redisTemplate.opsForSet().intersectAndStore(key, otherKey, destKey);
    }

    /**
     * 获取2个变量的合集。
     * @param key 键
     * @param otherKey 要合的键
     * @return 返回合并后的SET
     */
    @Override
    public Set<Object> union(String key, String otherKey) {
        return redisTemplate.opsForSet().union(key,otherKey);
    }

    /**
     *  获取多个变量的合集。
     * @param key 刍
     * @param list 多个变量LIST
     * @return 返回合集
     */
    @Override
    public Set<Object> union(String key, List list) {
        return redisTemplate.opsForSet().union(key,list);
    }

    /**
     * 获取2个变量合集后保存到最后一个参数上。
     * @param key 键
     * @param otherKey 要合的键
     * @param destKey 合并后的键
     */
    @Override
    public void unionAndStore(String key, String otherKey, String destKey) {
        redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
    }

    /**获取2个变量合集后保存到最后一个参数上。
     *
     * @param key 键
     * @param list 要合的键列表
     * @param destKey 合并后的键
     */
    @Override
    public void unionAndStore(String key, List list, String destKey) {
        redisTemplate.opsForSet().unionAndStore(key, list, destKey);
    }

    /**
     *  随机获取变量中的元素。
     * @param key 键
     * @return 返回其中一个随机元素
     */
    @Override
    public Object randomMember(String key){
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 随机获取变量中指定个数的元素
     * @param key 键
     * @param count 取随机数的个数
     * @return 返回随机数LIST
     */
    @Override
    public List<Object> randomMembers(String key, long count){
        return redisTemplate.opsForSet().randomMembers(key,count);
    }

    /**
     * 删除指定的KEY的缓存
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
    public boolean expire(String key, long seconds) {
        return redisTemplate.expire(key,seconds, TimeUnit.SECONDS);
    }

    @Override
    public long getExpire(String key) {
        return 0;
    }
}



