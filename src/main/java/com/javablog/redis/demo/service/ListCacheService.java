package com.javablog.redis.demo.service;

import java.util.List;

public interface ListCacheService extends CommonCacheService {

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @return
	 */
	public boolean lpushAll(String key, List <Object> value);

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public boolean lpushAll(String key, List <Object> value, long time);

	/**
	 * 往list左侧放数据
	 * 
	 * @param key
	 * @param object
	 * @return
	 */
	public Boolean lpush(String key, Object object);

	/**
	 * 往list右侧放数据
	 * 
	 * @param key
	 * @param object
	 * @return
	 */
	public Boolean rpush(String key, Object object);

	/**
	 * 往list左侧放数据
	 * 
	 * @param key
	 * @param objects
	 * @param expireTime
	 * @return
	 */
	public Boolean lpush(String key, int expireTime, Object... objects);

	/**
	 * 把最后一个参数值放到指定集合的第一个出现中间参数的前面，如果中间参数值存在的话
	 * @param key
	 * @param pivot
	 * @param object
	 * @return
	 */
	public Boolean lpush(String key, Object pivot,Object object);

	/**
	 * 往list右侧放数据
	 * 
	 * @param key
	 * @param objects
	 * @param expireTime
	 * @return
	 */
	public Boolean rpush(String key, int expireTime, Object... objects);

	/**
	 * 从list左侧拿数据，并转换为特定class,无阻塞
	 * 
	 * @param key
	 * @return
	 */
	public Object lpop(String key);



	/**
	 * 从list右侧拿数据，无阻塞
	 * 
	 * @param key
	 * @return
	 */
	public  Object rpop(String key);


	/**
	 * 获取队列所有数据 start,end指定取的范围 start=0,end=-1为取全部 start,end可为空，默认取全部
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public  List<Object> lrange(String key, long start, long end);


	/**
	 * 替换某个KEY值
	 * @param key
	 * @param index
	 * @param value
	 */
	public void set(String key, Long index, Object value) ;

	/**
	 * list长度
	 * 
	 * @param key
	 * @return
	 */
	public Long llen(String key);



	/**
	 * 根据索引取特定对象
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	
	public Object lindex(String key, Long index);

	/**
	 *   从存储在键中的列表中删除等于值的元素的第一个计数事件。count> 0：删除等于从左到右移动的值的第一个元素；count< 0：
	 *   删除等于从右到左移动的值的第一个元素；count = 0：删除等于value的所有元素。
	 * @param key
	 * @param count
	 * @param object
	 * @return 返回删除的数量
	 */
	public long remove(String key,long count,Object object) ;

	/**
	 *  如果存在集合则添加元素。
	 * @param key
	 * @param object
	 * @return
	 */
	public boolean lPushIfPresent(String key, Object object);

	/**
	 * 向集合中第一次出现第二个参数变量元素的右边添加第三个参数变量的元素值
	 * @param key
	 * @param pivot
	 * @param object
	 * @return
	 */
	public Boolean rpush(String key, Object pivot,Object object) ;

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @return
	 */
	public boolean rpushAll(String key, List <Object> value);

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public boolean rpushAll(String key, List <Object> value, long time) ;

	/**
	 *向已存在的集合中添加元素。
	 * @param key
	 * @param object
	 * @return
	 */
	public boolean rPushIfPresent(String key, Object object);

	/**
	 * 移除集合中左边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。
	 * @param key
	 * @param time
	 * @return
	 */
	public Object lpop(String key,long time) ;

	/**
	 *  移除集合中右边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。
	 * @param key
	 * @param time
	 * @return
	 */
	public Object rpop(String key,long time) ;

	/**
	 * 截取集合元素长度，保留长度内的数据。
	 * @param key
	 * @param start
	 * @param end
	 */
	public void trim(String key,long start,long end);

	/**'
	 *
	 * @param key
	 * @param str
	 * @return
	 */
	public Object rightPopAndLeftPush(String key,String str);

	/**
	 *
	 * @param key
	 * @param str
	 * @param timeout
	 * @return
	 */
	public Object rightPopAndLeftPush(String key,String str, long timeout);

}
