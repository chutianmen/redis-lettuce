package com.javablog.redis.demo;

import com.javablog.redis.demo.service.ListCacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CacheServiceApplication.class)
@WebAppConfiguration
public class ListCacheServiceImplTest {
    private final static Logger log = LoggerFactory.getLogger(ListCacheServiceImplTest.class);

    @Autowired
    private ListCacheService redisService;

    @Test
    public void testCacheList(){
        //在变量左边添加元素值。
        String key ="list" + System.currentTimeMillis();
        redisService.lpush(key,"a");
        redisService.lpush(key,"b");
        redisService.lpush(key,"c");
        List<Object> mylist = redisService.lrange(key, 0, -1);
        mylist.stream().forEach(System.out::println);

        //index  获取集合指定位置的值。
        String listValue = (String)redisService.lindex(key,2l) ;
        System.out.println("通过index(K key, long index)方法获取指定位置的值:" + listValue);

        //获取指定区间的值。
        List<Object> list = redisService.lrange(key,0,1);
        System.out.println("通过range(K key, long start, long end)方法获取指定范围的集合值:"+list);

        // 把最后一个参数,如下：“n”,值放到指定集合的第一个出现中间参数的前面，如果中间参数值存在的话。
        redisService.lpush(key,"a","n");
        list =  redisService.lrange(key,0,-1);
        System.out.println("通过leftPush(K key, V pivot, V value)方法把值放到指定参数值前面:" + list);

        // 向左边批量添加参数元素
        List<Object> list1= new ArrayList<Object>();
        list1.add("w");
        list1.add("x");
        list1.add("y");
        redisService.lpushAll(key, list1);
        list =  redisService.lrange(key,0,-1);
        System.out.println("通过leftPushAll(K key, V... values)方法批量添加元素:" + list);

        // 如果存在集合则向左边添加元素，不存在不加
        redisService.lPushIfPresent(key,"o");
        list =  redisService.lrange(key,0,-1);
        System.out.println("通过leftPushIfPresent(K key, V value)方法向已存在的集合添加元素:" + list);

        // 向集合最右边添加元素。
        redisService.rpush(key,"w");
        list =  redisService.lrange(key,0,-1);
        System.out.println("通过rightPush(K key, V value)方法向最右边添加元素:" + list);

        //向集合中第一次出现第二个参数变量元素的右边添加第三个参数变量的元素值。
        redisService.rpush(key,"w","r");
        list =  redisService.lrange(key,0,-1);
        System.out.println("通过rightPush(K key, V pivot, V value)方法向最右边添加元素:" + list);

        //向右边批量添加元素rpush
        List<Object> list2= new ArrayList<Object>();
        list2.add("j");
        list2.add("k");
        redisService.rpushAll(key,list2);
        list =  redisService.lrange(key,0,-1);
        System.out.println("通过rightPushAll(K key, V... values)方法向最右边批量添加元素:" + list);

        // 如果存在集合则向右边添加元素，不存在不加
        redisService.rPushIfPresent(key,"d");
        list =  redisService.lrange(key,0,-1);
        System.out.println("通过rightPushIfPresent(K key, V value)方法向已存在的集合添加元素:" + list);

        //获取集合长度
        long listLength = redisService.llen(key);
        System.out.println("通过size(K key)方法获取集合list的长度为:" + listLength);

        // 移除集合中的左边第一个元素
        Object popValue = redisService.lpop(key);
        System.out.print("通过leftPop(K key)方法移除的元素是:" + popValue);
        list =  redisService.lrange(key,0,-1);
        System.out.println(",剩余的元素是:" + list);

        // 移除集合中左边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。一般用在队列取值
        popValue = redisService.lpop(key,1000);
        System.out.print("通过leftPop(K key, long timeout, TimeUnit unit)方法移除的元素是:" + popValue);
        list =  redisService.lrange(key,0,-1);
        System.out.println(",剩余的元素是:" + list);

        // 移除集合中右边的元素。一般用在队列取值
        popValue = redisService.rpop(key);
        System.out.print("通过rPop(K key)方法移除的元素是:" + popValue);
        list =  redisService.lrange(key,0,-1);
        System.out.println(",剩余的元素是:" + list);

        // 移除集合中右边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。一般用在队列取值
        popValue = redisService.rpop(key,1000);
        System.out.print("通过leftPop(K key, long timeout, TimeUnit unit)方法移除的元素是:" + popValue);
        list =  redisService.lrange(key,0,-1);
        System.out.println(",剩余的元素是:" + list);

        //移除集合中右边的元素，同时在左边加入一个元素。
        popValue = redisService.rightPopAndLeftPush(key,"12");
        System.out.print("通过rightPopAndLeftPush(K sourceKey, K destinationKey)方法移除的元素是:" + popValue);
        list =  redisService.lrange(key,0,-1);
        System.out.println(",剩余的元素是:" + list);

        //移除集合中右边的元素在等待的时间里，同时在左边添加元素，如果超过等待的时间仍没有元素则退出。
        popValue = redisService.rightPopAndLeftPush(key,"13",1);
        System.out.println("通过rightPopAndLeftPush(K sourceKey, K destinationKey, long timeout)方法移除的元素是:" + popValue);
        list =  redisService.lrange("presentList",0,-1);
        System.out.print(",剩余的元素是:" + list);

        // 在集合的指定位置插入元素,如果指定位置已有元素，则覆盖，没有则新增，超过集合下标+n则会报错。
        redisService.set(key,3l,"15");
        list =  redisService.lrange(key,0,-1);
        System.out.print("通过set(K key, long index, V value)方法在指定位置添加元素后:" + list);

        //从存储在键中的列表中删除等于值的元素的第一个计数事件。count> 0：
        //删除等于从左到右移动的值的第一个元素；count< 0：删除等于从右到左移动的值的第一个元素；count = 0：删除等于value的所有元素。
        long removeCount = redisService.remove(key,0,"w");
        list =  redisService.lrange(key,0,-1);
        System.out.println("通过remove(K key, long count, Object value)方法移除元素数量:" + removeCount);
        System.out.println(",剩余的元素:" + list);

        // 截取集合元素长度，保留长度内的数据。
        redisService.trim(key,0,5);
        list =  redisService.lrange(key,0,-1);
        System.out.println("通过trim(K key, long start, long end)方法截取后剩余元素:" + list);

        //删除
        redisService.del(key);
        System.out.println("通过del(String... keys) 方法操作后:" + list);

    }

    //学生练习题 ，通过REDIS实现队列先进先出的效果
    @Test
    public void testRedisQueue(){
        //队列名称
        String queueKey = "topic";
        //入队列
        for (int i=0;i<=100;i++){
            User user =new User();
            user.setUserId(String.valueOf(i));
            user.setName("name" + i);
            user.setAge(i);
            redisService.lpush(queueKey,user);
        }
        //出队列
        for (int i=0;i<=100;i++){
            User user = (User)redisService.rpop(queueKey);
            System.out.println(user.toString());
        }
    }


}