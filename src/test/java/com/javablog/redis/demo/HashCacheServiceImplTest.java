package com.javablog.redis.demo;

import com.javablog.redis.demo.service.HashCacheService;
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

import java.util.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CacheServiceApplication.class)
@WebAppConfiguration
public class HashCacheServiceImplTest {
    private final static Logger log = LoggerFactory.getLogger(HashCacheServiceImplTest.class);

    @Autowired
    private HashCacheService redisService;

    @Test
    public void testSetGetString(){
        HashMap<String, Object> map =new HashMap<String, Object>();
        map.put("appkey","343kksfsdfsdfsdf");
        map.put("pv",10l);
        map.put("appName","千锋");
        String key = "appid:1234567";

        //hmset
        redisService.hmset(key, map);

        //hmget 获取变量中的键值对。
        HashMap<Object, Object> map1 = (HashMap<Object, Object>) redisService.hmget(key);
        System.out.println("查询结果:" + map1);

        //hset  新增hashMap值
        redisService.hset(key,"addr","北京");

        //hmget 获取变量中的键值对。
        map1 = (HashMap<Object, Object>) redisService.hmget(key);
        System.out.println("查询结果:" + map1);

        //values 获取指定变量中的hashMap值。
        List<Object> hashList = redisService.values(key);
        System.out.println("通过values(H key)方法获取变量中的hashMap值:" + hashList);

        //hget 获取变量中的指定map键是否有值,如果存在该map键则获取值，没有则返回null。
        String appName = (String)redisService.hget(key,"appName");
        System.out.println("通过get(H key, Object hashKey)方法获取map键的值:" + appName);

        //hasKey 判断变量中是否有指定的map键。
        boolean hashKeyBoolean = redisService.hHasKey(key,"appName");
        System.out.println("通过hasKey(H key, Object hashKey)方法判断变量中是否存在map键:" + hashKeyBoolean);

        //key 获取变量中的键。
        Set<Object> keySet = redisService.keys(key);
        System.out.println("通过keys(H key)方法获取变量中的键:" + keySet);

        //size  获取变量的长度。
        long hashLength = redisService.size(key);
        System.out.println("通过size(H key)方法获取变量的长度:" + hashLength);

        //hincr 使变量中的键以double值的大小进行自增长。
        redisService.hincr(key, "pv" , 100l);
        Integer pv = (Integer)redisService.hget(key,"pv");
        System.out.println("查询pv:" + pv);

        //hdecr
        redisService.hdecr(key, "pv" , 100l);
        pv = (Integer)redisService.hget(key,"pv");
        System.out.println("查询减少后的pv:" + pv);

        //multiGet 以集合的方式获取变量中的值。
        List<Object> list = new ArrayList<Object>();
        list.add("pv");
        list.add("addr");
        List mapValueList = redisService.multiGet(key,list);
        System.out.println("通过multiGet(H key, Collection<HK> hashKeys)方法以集合的方式获取变量中的值:"+mapValueList);

        //putIfAbsent 如果变量值存在，在变量中可以添加不存在的的键值对，
        // 如果变量不存在，则新增一个变量，同时将键值对添加到该变量。
        redisService.putIfAbsent(key,"tel","8855222");
        map1 = (HashMap<Object, Object>) redisService.hmget(key);
        System.out.println("通过putIfAbsent(H key, HK hashKey, HV value)方法添加不存在于变量中的键值对:" + map1);

        //scan 匹配获取键值对，ScanOptions.NONE为获取全部键对，ScanOptions.scanOptions().match("map1").build()
        // 匹配获取键位map1的键值对,不能模糊匹配。
//        Cursor<Map.Entry<Object,Object>> cursor = redisService.scan(key, ScanOptions.scanOptions().match("pv").build());
        Cursor<Map.Entry<Object,Object>> cursor = redisService.scan(key,ScanOptions.NONE);
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            System.out.println("通过scan(H key, ScanOptions options)方法获取匹配键值对:" + entry.getKey() + "---->" + entry.getValue());
        }

        //hdel 使变量中的键以double值的大小进行自减少。
        redisService.hdel(key,"pv","addr");
        HashMap<Object, Object> map2 = (HashMap<Object, Object>) redisService.hmget(key);
        System.out.println("查询删除部分KEY后的结果:" + map2);

        //del 删除
        redisService.del(key);
        map2 = (HashMap<Object, Object>) redisService.hmget(key);
        System.out.println("查询删除缓存的结果:" + map2);
    }

}