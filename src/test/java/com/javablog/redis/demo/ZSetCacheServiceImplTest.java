package com.javablog.redis.demo;

import com.javablog.redis.demo.service.ZSetCacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CacheServiceApplication.class)
@WebAppConfiguration
public class ZSetCacheServiceImplTest {
    private final static Logger log = LoggerFactory.getLogger(ZSetCacheServiceImplTest.class);

    @Autowired
    private ZSetCacheService redisService;

    @Test
    public void testCacheZSet(){
        //add
        String key = "zSetValue" + System.currentTimeMillis();
        redisService.add(key,"A",1.0);
        redisService.add(key,"B",2.0);
        redisService.add(key,"C",3.0);
        redisService.add(key,"D",4.0);
        redisService.add(key,"Z",500.0);

        //获取变量指定区间的元素。
        Set zSetValue = redisService.range(key,0,-1);
        System.out.println("通过range(K key, long start, long end)方法获取指定区间的元素:" + zSetValue);

        //用于获取满足非score的排序取值。这个排序只有在有相同分数的情况下才能使用，如果有不同的分数则返回值不确定。
        String key1 = "zSetValue1" + System.currentTimeMillis();
        redisService.add(key1,"C",1.0);
        redisService.add(key1,"D",1.0);
        redisService.add(key1,"A",1.0);
        redisService.add(key1,"B",1.0);
        RedisZSetCommands.Range range = new RedisZSetCommands.Range();
        zSetValue = redisService.rangeByLex(key1, range);
        System.out.println("通过rangeByLex(K key, RedisZSetCommands.Range range)方法获取满足非score的排序取值元素:" + zSetValue);

        //用于获取满足非score的设置下标开始的长度排序取值。
        RedisZSetCommands.Limit limit = new RedisZSetCommands.Limit();
        limit.count(2);
        //起始下标为0
        limit.offset(1);
        zSetValue = redisService.rangeByLex(key, range,limit);
        System.out.println("通过rangeByLex(K key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit)方法获取满足非score的排序取值元素:" + zSetValue);

        //通过TypedTuple方式新增数据。
        ZSetOperations.TypedTuple<Object> typedTuple1 = new DefaultTypedTuple<Object>("E",6.0);
        ZSetOperations.TypedTuple<Object> typedTuple2 = new DefaultTypedTuple<Object>("F",7.0);
        ZSetOperations.TypedTuple<Object> typedTuple3 = new DefaultTypedTuple<Object>("G",5.0);
        Set<ZSetOperations.TypedTuple<Object>> typedTupleSet = new HashSet<ZSetOperations.TypedTuple<Object>>();
        typedTupleSet.add(typedTuple1);
        typedTupleSet.add(typedTuple2);
        typedTupleSet.add(typedTuple3);
        redisService.add(key,typedTupleSet);
        zSetValue = redisService.range(key,0,-1);
        System.out.println("通过add(K key, Set<ZSetOperations.TypedTuple<V>> tuples)方法添加元素:" + zSetValue);

        // 根据设置的score获取区间值。
        zSetValue = redisService.rangeByScore(key,1,2);
        System.out.println("通过rangeByScore(K key, double min, double max)方法根据设置的score获取区间值:" + zSetValue);

        //根据设置的score获取区间值从给定下标和给定长度获取最终值。
        zSetValue = redisService.rangeByScore(key,1,5,1,3);
        System.out.println("通过rangeByScore(K key, double min, double max, long offset, long count)方法根据设置的score获取区间值:" + zSetValue);

        //获取RedisZSetCommands.Tuples的区间值。
        typedTupleSet = redisService.rangeWithScores(key,1,3);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = typedTupleSet.iterator();
        while (iterator.hasNext()){
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            Object value = typedTuple.getValue();
            double score = typedTuple.getScore();
            System.out.println("通过rangeWithScores(K key, long start, long end)方法获取RedisZSetCommands.Tuples的区间值:" + value + "---->" + score );
        }

        //获取RedisZSetCommands.Tuples的区间值通过分值。
        typedTupleSet = redisService.rangeByScoreWithScores(key,6,8);
        iterator = typedTupleSet.iterator();
        while (iterator.hasNext()){
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            Object value = typedTuple.getValue();
            double score = typedTuple.getScore();
            System.out.println("通过rangeByScoreWithScores(K key, double min, double max)方法获取RedisZSetCommands.Tuples的区间值通过分值:" + value + "---->" + score );
        }

        // 获取区间值的个数。
        long count = redisService.count(key,1,5);
        System.out.println("通过count(K key, double min, double max)方法获取区间值的个数:" + count);

        // 获取变量中元素的索引,下标开始位置为0。
        long index = redisService.rank(key,"B");
        System.out.println("通过rank(K key, Object o)方法获取变量中元素的索引:" + index);

        //匹配获取键值对，ScanOptions.NONE为获取全部键值对；ScanOptions.scanOptions().match("C").build()匹配获取键位map1的键值对,不能模糊匹配。
        //Cursor<Object> cursor = redisTemplate.opsForSet().scan("setValue", ScanOptions.NONE);
        Cursor<ZSetOperations.TypedTuple<Object>> cursor =redisService.scan(key, ScanOptions.NONE);
        while (cursor.hasNext()){
            ZSetOperations.TypedTuple<Object> typedTuple = cursor.next();
            System.out.println("通过scan(K key, ScanOptions options)方法获取匹配元素:" + typedTuple.getValue() + "--->" + typedTuple.getScore());
        }

        //  获取元素的分值。
        double score = redisService.score(key,"B");
        System.out.println("通过score(K key, Object o)方法获取元素的分值:" + score);

        //获取变量中元素的个数。
        long zCard = redisService.zCard(key);
        System.out.println("通过zCard(K key)方法获取变量的长度:" + zCard);

        //修改变量中的元素的分值。
        double incrementScore = redisService.incrementScore(key,"C",5);
        System.out.print("通过incrementScore(K key, V value, double delta)方法修改变量中的元素的分值:" + incrementScore);
        score = redisService.score(key,"C");
        System.out.print(",修改后获取元素的分值:" + score);
        zSetValue = redisService.range(key,0,-1);
        System.out.println("，修改后排序的元素:" + zSetValue);

        //索引倒序排列指定区间元素。
        zSetValue = redisService.reverseRange(key,0,-1);
        System.out.println("通过reverseRange(K key, long start, long end)方法倒序排列元素:" + zSetValue);

        //倒序排列指定分值区间元素。
        zSetValue = redisService.reverseRangeByScore(key,1,5);
        System.out.println("通过reverseRangeByScore(K key, double min, double max)方法倒序排列指定分值区间元素:" + zSetValue);

        //倒序排列从给定下标和给定长度分值区间元素。
        zSetValue = redisService.reverseRangeByScore(key,1,5,1,2);
        System.out.println("通过reverseRangeByScore(K key, double min, double max, long offset, long count)方法倒序排列从给定下标和给定长度分值区间元素:" + zSetValue);

        // 倒序排序获取RedisZSetCommands.Tuples的分值区间值。
        typedTupleSet = redisService.reverseRangeByScoreWithScores(key,1,5);
        iterator = typedTupleSet.iterator();
        while (iterator.hasNext()) {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            Object value = typedTuple.getValue();
            double score1 = typedTuple.getScore();
            System.out.println("通过reverseRangeByScoreWithScores(K key, double min, double max)方法倒序排序获取RedisZSetCommands.Tuples的区间值:" + value + "---->" + score1);
        }

        // 获取倒序排列的索引值。
        long reverseRank = redisService.reverseRank(key,"B");
        System.out.println("通过reverseRank(K key, Object o)获取倒序排列的索引值:" + reverseRank);

        // 获取2个变量的交集存放到第3个变量里面。
        String otherkey = "typedTupleSet" + System.currentTimeMillis();
        redisService.add(otherkey,"A",1.0);
        redisService.intersectAndStore(key,otherkey,"intersectSet");
        zSetValue = redisService.range("intersectSet",0,-1);
        System.out.println("通过intersectAndStore(K key, K otherKey, K destKey)方法获取2个变量的交集存放到第3个变量里面:" + zSetValue);

        // 获取多个变量的交集存放到第3个变量里面。
        List list = new ArrayList();
        list.add(key1);
        redisService.intersectAndStore(key,list,"intersectListSet");
        zSetValue = redisService.range("intersectListSet",0,-1);
        System.out.println("通过intersectAndStore(K key, Collection<K> otherKeys, K destKey)方法获取多个变量的交集存放到第3个变量里面:" + zSetValue);

        // 索引倒序排列区间值。
        typedTupleSet = redisService.reverseRangeWithScores(key,1,2);
        iterator = typedTupleSet.iterator();
        while (iterator.hasNext()) {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            Object value = typedTuple.getValue();
            double score1 = typedTuple.getScore();
            System.out.println("通过reverseRangeWithScores(K key, long start, long end)方法索引倒序排列区间值:" + value + "----->" + score1);
        }

        //获取2个变量的合集存放到第3个变量里面。
        redisService.unionAndStore(key,"typedTupleSet","unionSet");
        zSetValue = redisService.range("unionSet",0,-1);
        System.out.println("通过unionAndStore(K key, K otherKey, K destKey)方法获取2个变量的合集存放到第3个变量里面:" + zSetValue);

        // 获取多个变量的合集存放到第3个变量里面。
        list = new ArrayList();
        list.add("typedTupleSet");
        redisService.unionAndStore(key, list,"unionListSet");
        zSetValue = redisService.range("unionListSet",0,-1);
        System.out.println("通过unionAndStore(K key, Collection<K> otherKeys, K destKey)方法获取多个变量的合集存放到第3个变量里面:" + zSetValue);

        //批量移除元素根据元素值。
        long removeCount = redisService.remove(key,"A","B");
        zSetValue = redisService.range(key,0,-1);
        System.out.print("通过remove(K key, Object... values)方法移除元素的个数:" + removeCount);
        System.out.println(",移除后剩余的元素:" + zSetValue);

        //根据分值移除区间元素。
        removeCount = redisService.removeRangeByScore(key,3,5);
        zSetValue = redisService.range(key,0,-1);
        System.out.print("通过removeRangeByScore(K key, double min, double max)方法移除元素的个数:" + removeCount);
        System.out.println(",移除后剩余的元素:" + zSetValue);

        // 根据索引值移除区间元素。
        removeCount = redisService.removeRange(key,3,5);
        zSetValue = redisService.range(key,0,-1);
        System.out.print("通过removeRange(K key, long start, long end)方法移除元素的个数:" + removeCount);
        System.out.println(",移除后剩余的元素:" + zSetValue);

        //删除
        redisService.del(key);
        zSetValue = redisService.range(key,0,-1);
        System.out.println("通过del(String... keys) 方法后查询结果:" + zSetValue);
    }


}