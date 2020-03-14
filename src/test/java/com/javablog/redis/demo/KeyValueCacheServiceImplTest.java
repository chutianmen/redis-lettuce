package com.javablog.redis.demo;

import com.javablog.redis.demo.service.KeyValueCacheService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CacheServiceApplication.class)
@WebAppConfiguration
public class KeyValueCacheServiceImplTest {
    private final static Logger log = LoggerFactory.getLogger(KeyValueCacheServiceImplTest.class);

    @Autowired
    private KeyValueCacheService redisService;

    //普通缓存测试
    @Test
    public void testSetGetString(){
        redisService.set("key","value",1000);
        String str = redisService.get("key1");
        System.out.println("查询结果:" + str);

        //在原有的值基础上新增字符串到末尾。 redisTemplate读不出APPEND的值，stringRedisTemplate可以
        redisService.append("key","add string");
        str = redisService.get("key");
        System.out.println("通过append(K key, String value)方法修改后的字符串:" + str);

        //截取key键对应值得字符串，从开始下标位置开始到结束下标的位置(包含结束下标)的字符串。
        String cutString = redisService.get("key",0,4);
        System.out.println("通过get(K key, long start, long end)方法获取截取的字符串:"+cutString);

        // 获取原来key键对应的值并重新赋新值。
        String oldAndNewStringValue = (String)redisService.getAndSet("key","new value");
        System.out.print("通过getAndSet(K key, V value)方法获取原来的" + oldAndNewStringValue + ",");
        String newStringValue = redisService.get("key");
        System.out.println("修改过后的值:"+newStringValue);

        //获取指定字符串的长度。
        Long stringValueLength = redisService.size("key");
        System.out.println("通过size(K key)方法获取字符串的长度:"+stringValueLength);

        //如果键不存在则新增,存在则不改变已经有的值。
        boolean absentBoolean = redisService.setIfAbsent("absentKey","fff");
        System.out.println("通过setIfAbsent(K key, V value)方法判断变量值absentValue是否存在:" + absentBoolean);
        if(absentBoolean) {
            String absentValue = redisService.get("absentKey") + "";
            System.out.print(",不存在，则新增后的值是:" + absentValue);
            boolean existBoolean = redisService.setIfAbsent("absentKey", "eee");
            System.out.println(",再次调用setIfAbsent(K key, V value)判断absentValue是否存在并重新赋值:" + existBoolean);
            if (!existBoolean) {
                absentValue = redisService.get("absentKey") + "";
                System.out.println("如果存在,则重新赋值后的absentValue变量的值是:" + absentValue);
            }
        }

        redisService.del("key");
        System.out.println("删除后再查结果:" + redisService.get("key"));
    }

    //集合功能测试
    @Test
    public void testmulti() {
        Map valueMap = new HashMap();
        valueMap.put("valueMap1", "map1");
        valueMap.put("valueMap2", "map2");
        valueMap.put("valueMap3", "map3");
        redisService.multiSet(valueMap);

        //根据集合取出对应的value值。
        List paraList = new ArrayList();
        paraList.add("valueMap1");
        paraList.add("valueMap2");
        paraList.add("valueMap3");
        List<String> valueList = redisService.multiGet(paraList);
        for (String value : valueList) {
            System.out.println("通过multiGet(Collection<K> keys)方法获取map值:" + value);
        }
    }

    //针对复杂的对象类型测试
    @Test
    public void testSetGetObject(){
        User user =new User();
        user.setAge(22);
        user.setName("张三");
        user.setUserId("1234567");
        redisService.set("userid:1234567",user,1000);
        User userEntity = (User) redisService.getObject("userid:1234567");
        System.out.println("user info>>>>>>>>>>>>>>>>>:" + userEntity.toString());
        TestCase.assertNotSame(user,userEntity);
    }

    // 过期测试
    @Test
    public void testSetKeyExpire() throws InterruptedException {
        redisService.set("expirekey","值");
        redisService.expire("expirekey" ,30);
        System.out.println("超时前取值:" + redisService.get("expirekey"));
        System.out.println("获取过期时间:" + redisService.getExpire("expirekey")+"秒") ;
        Thread.sleep(5000);
        String str = redisService.get("expirekey");
        System.out.println("超时后取值:"  + redisService.get("expirekey"));
    }

    // 计数测试
    @Test
    public void testIncrDecr() throws InterruptedException {
        redisService.incr("count:pv",10000);
        System.out.println("统计PV的数值:" + redisService.getObject("count:pv"));
        redisService.decr("count:pv",5000);
        Integer count = (Integer)redisService.getObject("count:pv");
        System.out.println("统计decr后的PV的数值:"  + count);
    }
}