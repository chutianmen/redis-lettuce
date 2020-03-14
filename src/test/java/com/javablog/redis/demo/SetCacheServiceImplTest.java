package com.javablog.redis.demo;

import com.javablog.redis.demo.service.SetCacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CacheServiceApplication.class)
@WebAppConfiguration
public class SetCacheServiceImplTest {
    private final static Logger log = LoggerFactory.getLogger(SetCacheServiceImplTest.class);

    @Autowired
    private SetCacheService redisService;

    @Test
    public void testCacheSet(){
        //add 观察有重复的数会去重
        String key = "setkey";
        redisService.add(key,"A","B","C","B","D","E","F");

        //members(K key)获取变量中的值。
        Set set = redisService.members(key);
        System.out.println("通过members(K key)方法获取变量中的元素值:" + set);

        //获取变量中值的长度。
        long setLength = redisService.size(key);
        System.out.println("通过size(K key)方法获取变量中元素值的长度:" + setLength);

        // 随机获取变量中的元素。
        Object randomMember = redisService.randomMember(key);
        System.out.println("通过randomMember(K key)方法随机获取变量中的元素:" + randomMember);

        // 随机获取变量中指定个数的元素。
        List randomMembers = redisService.randomMembers(key,3);
        System.out.println("通过randomMembers(K key, long count)方法随机获取变量中指定个数的元素:" + randomMembers);

        // 检查给定的元素是否在变量中。
        boolean isMember = redisService.isMember(key,"A");
        System.out.println("通过isMember(K key, Object o)方法检查给定的元素是否在变量中:" + isMember);

        //转移变量的元素值到目的变量。
        boolean isMove = redisService.move(key,"A","destSetValue");
        if(isMove){
            set = redisService.members(key);
            System.out.print("通过move(K key, V value, K destKey)方法转移变量的元素值到目的变量后的剩余元素:" + set);
            set = redisService.members("destSetValue");
            System.out.println(",目的变量中的元素值:" + set);
        }

        //弹出变量中的元素。
        Object popValue = redisService.pop(key);
        System.out.print("通过pop(K key)方法弹出变量中的元素:" + popValue);
        set = redisService.members(key);
        System.out.println(",剩余元素:" + set);

        // 批量移除变量中的元素。
        long removeCount = redisService.remove(key,"E","F","G");
        System.out.print("通过remove(K key, Object... values)方法移除变量中的元素个数:" + removeCount);
        set = redisService.members(key);
        System.out.println(",剩余元素:" + set);

        //匹配获取键值对，ScanOptions.NONE为获取全部键值对；ScanOptions.scanOptions().match("C").build()匹配获取键位map1的键值对,不能模糊匹配。?
        Cursor<Object> cursor = redisService.scan(key, ScanOptions.NONE);
//        Cursor<Object> cursor = redisService.scan(key, ScanOptions.scanOptions().match("C").build());
        while (cursor.hasNext()){
            Object object = cursor.next();
            System.out.println("通过scan(K key, ScanOptions options)方法获取匹配的值:" + object);
        }

        //通过集合求差值。
        List<Object> list = new ArrayList();
        list.add("destSetValue");
//        redisService.add("destSetValue","B");
        Set set1 = redisService.members("destSetValue");
        System.out.println("通过members(K key)方法获取变量destSetValue中的元素值:" + set1);
        Set differenceSet = redisService.difference(key,list);
        System.out.println("通过difference(K key, Collection<K> otherKeys)方法获取变量中与给定集合中变量不一样的值:" + differenceSet);

        // 通过给定的key求2个set变量的差值。
        differenceSet = redisService.difference(key,"destSetValue");
        System.out.println("通过difference(K key, Collection<K> otherKeys)方法获取变量中与给定变量不一样的值:" + differenceSet);

        //将求出来的差值元素保存。
        redisService.differenceAndStore(key,"destSetValue","storeSetValue");
        set = redisService.members("storeSetValue");
        System.out.println("通过differenceAndStore(K key, K otherKey, K destKey)方法将求出来的差值元素保存:" + set);

        //将求出来的差值元素保存。
        redisService.differenceAndStore(key,list,"storeSetValue");
        set = redisService.members("storeSetValue");
        System.out.println("通过differenceAndStore(K key, Collection<K> otherKeys, K destKey)方法将求出来的差值元素保存:" + set);

        //获取去重的随机元素。
        set = redisService.distinctRandomMembers(key,2);
        System.out.println("通过distinctRandomMembers(K key, long count)方法获取去重的随机元素:" + set);

        //获取2个变量中的交集。
        set = redisService.intersect(key,"destSetValue");
        System.out.println("通过intersect(K key, K otherKey)方法获取交集元素:" + set);

        // 获取多个变量之间的交集。
        set = redisService.intersect(key,list);
        System.out.println("通过intersect(K key, Collection<K> otherKeys)方法获取交集元素:" + set);

        //获取2个变量交集后保存到最后一个参数上。
        redisService.intersectAndStore(key,"destSetValue","intersectValue");
        set = redisService.members("intersectValue");
        System.out.println("通过intersectAndStore(K key, K otherKey, K destKey)方法将求出来的交集元素保存:" + set);

        //获取多个变量的交集并保存到最后一个参数上。
        redisService.intersectAndStore(key,list,"intersectListValue");
        set = redisService.members("intersectListValue");
        System.out.println("通过intersectAndStore(K key, Collection<K> otherKeys, K destKey)方法将求出来的交集元素保存:" + set);

        //获取2个变量的合集。
        set = redisService.union(key,"destSetValue");
        System.out.println("通过union(K key, K otherKey)方法获取2个变量的合集元素:" + set);

        //  获取多个变量的合集。
        set = redisService.union(key,list);
        System.out.println("通过union(K key, Collection<K> otherKeys)方法获取多个变量的合集元素:" + set);

        //获取2个变量合集后保存到最后一个参数上。
        redisService.unionAndStore(key,"destSetValue","unionValue");
        set = redisService.members("unionValue");
        System.out.println("通过unionAndStore(K key, K otherKey, K destKey)方法将求出来的交集元素保存:" + set);

        //获取多个变量的合集并保存到最后一个参数上。
        redisService.unionAndStore(key,list,"unionListValue");
        set = redisService.members("unionListValue");
        System.out.println("通过unionAndStore(K key, Collection<K> otherKeys, K destKey)方法将求出来的交集元素保存:" + set);

        //删除
        redisService.del(key);
        set = redisService.members(key);
        System.out.println("通过del(String... keys) 方法后查询结果:" + set);
    }


}