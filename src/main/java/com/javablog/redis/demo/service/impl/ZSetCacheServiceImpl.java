package com.javablog.redis.demo.service.impl;

import com.javablog.redis.demo.service.ZSetCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Service("zsetCacheService")
public class ZSetCacheServiceImpl implements ZSetCacheService {
    private final static Logger log = LoggerFactory.getLogger(ZSetCacheServiceImpl.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 增添加元素到变量中同时指定元素的分值。
     * @param key 键
     * @param value 值
     * @param score 分值
     * @return true 成功 false 失败
     */
    public boolean add(String key, Object value, double score){
        try {
            redisTemplate.opsForZSet().add(key,value,score);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取变量指定区间的元素。START为0,END为-1代表取全部
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     * @return 返回SET
     */
    @Override
    public Set<Object> range(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key,start,end);
    }

    /**
     * 用于获取满足非score的排序取值。这个排序只有在有相同分数的情况下才能使用，如果有不同的分数则返回值不确定。
     * @param key 键
     * @param range
     * @return 返回SET
     */
    public Set<Object> rangeByLex(String key, RedisZSetCommands.Range range){
        return redisTemplate.opsForZSet().rangeByLex(key,range);
    }

    /**
     * 获取变量中元素的个数
     * @param key 键
     * @return 返回个数
     */
    public long zCard(String key){
        return redisTemplate.opsForZSet().zCard(key);
    }

    /**
     * 获取区间值的个数。
     * @param key 键
     * @param min 最小SCORE
     * @param max 最大SCORE
     * @return 返回数量
     */
    @Override
    public long count(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key,min,max);
    }

    /**
     * 修改变量中的元素的分值。
     * @param key
     * @param value
     * @param delta
     * @return
     */
    @Override
    public double incrementScore(String key, Object value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key,value,delta);
    }

    /**
     * 获取元素的分值
     * @param key 键
     * @param o 要查找的值
     * @return 返回分值
     */
    public double score(String key, Object o){
        return redisTemplate.opsForZSet().score(key,o);
    }

    /**
     * 用于获取满足非score的设置下标开始的长度排序取值。
     * @param key 键
     * @param range 范围
     * @param limit 限制区域
     * @return 返回SET
     */
    @Override
    public Set<Object> rangeByLex(String key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        return redisTemplate.opsForZSet().rangeByLex(key, range,limit);
    }

    /**
     * 通过TypedTuple方式新增数据。
     * @param key 键
     * @param tuples 元组
     */
    @Override
    public void add(String key, Set<ZSetOperations.TypedTuple<Object>> tuples) {
        redisTemplate.opsForZSet().add(key,tuples);
    }

    /**
     * 根据设置的score获取区间值
     * @param key 键
     * @param min 最小值
     * @param max 最大值
     * @return 返回SET
     */
    @Override
    public Set<Object> rangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key,min,max);
    }

    /**
     * 根据设置的score获取区间值从给定下标和给定长度获取最终值。
     * @param key 键
     * @param min 最小值
     * @param max 最大值
     * @param offset 偏移时
     * @param count 取的长度
     * @return 返回SET
     */
    @Override
    public Set<Object> rangeByScore(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().rangeByScore(key,min,max,offset,count);
    }

    /**
     * 获取RedisZSetCommands.Tuples的区间值。
     * @param key 键
     * @param start 开始SCORE值
     * @param end   结束SCORE值
     * @return 返回区间值
     */
    @Override
    public Set<ZSetOperations.TypedTuple<Object>> rangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key,start,end);
    }

    /**
     * 获取RedisZSetCommands.Tuples的区间值通过分值。
     * @param key 键
     * @param min 最小分值
     * @param max 最大分值
     * @return 返回SET
     */
    @Override
    public Set<ZSetOperations.TypedTuple<Object>> rangeByScoreWithScores(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
    }

    /**
     * 获取RedisZSetCommands.Tuples的区间值从给定下标和给定长度获取最终值通过分值。
     * @param key 键
     * @param min 最小分值
     * @param max 最大分值
     * @param offset 偏移量
     * @param count 总数
     * @return 返回SET
     */
    @Override
    public Set<ZSetOperations.TypedTuple<Object>> rangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max,offset,count);
    }

    /**
     * 获取变量中元素的索引,下标开始位置为
     * @param key 键
     * @param o 要查找的值
     * @return 返回下标
     */
    @Override
    public long rank(String key, Object o) {
        return redisTemplate.opsForZSet().rank(key,o);
    }

    /**
     * 匹配获取键值对，ScanOptions.NONE为获取全部键值对；ScanOptions.scanOptions().match("C").build()匹配获取键位map1的键值对,不能模糊匹配。
     * @param key 键
     * @param options 选项
     * @return 返回键值对
     */
    @Override
    public Cursor<ZSetOperations.TypedTuple<Object>> scan(String key, ScanOptions options) {
        return redisTemplate.opsForZSet().scan(key, options);
    }

    /**
     * 索引倒序排列指定区间元素。
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     * @return 返回倒排后的结果
     */
    @Override
    public Set<Object> reverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key,start,end);
    }

    /**
     * 倒序排列指定分值区间元素。
     * @param key 键
     * @param min 最小SCORE
     * @param max 最大SCORE
     * @return 返回区间元素
     */
    @Override
    public Set<Object> reverseRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key,min,max);
    }

    /**
     * 倒序排列从给定下标和给定长度分值区间元素。
     * @param key 键
     * @param min 最小SCORE
     * @param max 最大SCORE
     * @param offset 偏移量
     * @param count  数量
     * @return 返回列表
     */
    @Override
    public Set<Object> reverseRangeByScore(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key,min,max,offset,count);
    }

    /**
     * 倒序排序获取RedisZSetCommands.Tuples的分值区间值。
     * @param key 键
     * @param min 最小SCORE
     * @param max 最大SCORE
     * @return 返回SET集合
     */
    @Override
    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeByScoreWithScores(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key,min,max);
    }

    /**
     * 序排序获取RedisZSetCommands.Tuples的从给定下标和给定长度分值区间值
     * @param key 键
     * @param min 最小SCORE
     * @param max 最大SCORE
     * @param offset 偏移量
     * @param count 总数
     * @return 返回SET
     */
    @Override
    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key,min,max,offset,count);
    }

    /**
     * 索引倒序排列区间值。
     * @param key 键
     * @param start 开始Score
     * @param end   结束SCORE
     * @return      返回列表
     */
    @Override
    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key,start,end);
    }

    /**
     * 获取倒序排列的索引值。
     * @param key 键
     * @param o   值
     * @return    返回倒序排列的索引值
     */
    @Override
    public long reverseRank(String key, Object o) {
        return redisTemplate.opsForZSet().reverseRank(key,o);
    }

    /**
     * 获取2个变量的交集存放到第3个变量里面。
     * @param key 键
     * @param otherKey 要交集的键
     * @param destKey 目标键
     * @return 返回交集长度
     */
    @Override
    public long intersectAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key,otherKey,destKey);
    }

    /**
     * 获取多个变量的交集存放到第3个变量里面。
     * @param key 键
     * @param list 多个要交集的KEY
     * @param destKey 要存入的KEY
     * @return 返回数量
     */
    @Override
    public long intersectAndStore(String key, List list, String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key,list,destKey);
    }

    /**
     * 获取2个变量的合集存放到第3个变量里面。
     * @param key 键
     * @param otherKey 要合并的KEY
     * @param destKey  共同的并集元素存到destK
     * @return 返回元素个数
     */
    @Override
    public long unionAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key,otherKey,destKey);
    }

    /**
     * 获取多个变量的合集存放到第3个变量里面。
     * @param key 键
     * @param list 要合的集合KEY
     * @param destKey 目票集合KEY
     * @return 返回合集长度
     */
    @Override
    public long unionAndStore(String key, List list, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key,list,destKey);
    }

    /**
     * 批量移除元素根据元素值。
     * @param key 键
     * @param values 要删除的元素
     * @return 返回删除的数量
     */
    @Override
    public long remove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key,values);
    }

    /**
     * 根据分值移除区间元素。
     * @param key 键
     * @param min 最小的SCORE
     * @param max 最大的SCORE
     * @return 返回移除的元素数量
     */
    @Override
    public long removeRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key,min,max);
    }

    /**
     * 根据索引值移除区间元素。
     * @param key 键
     * @param start 索引开始
     * @param end   索引结束
     * @return 返回移除的数量
     */
    @Override
    public long removeRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().removeRange(key,start,end);
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
        return false;
    }

    @Override
    public long getExpire(String key) {
        return 0;
    }
}



