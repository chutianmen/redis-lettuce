package com.javablog.redis.demo.service;

import java.util.List;
import java.util.Map;

public interface KeyValueCacheService extends CommonCacheService {

	/**
	 * 普通缓存放入
	 * @param key 键
	 * @param value 值
	 * @param expireTime 超时时间(秒)
	 * @return true成功 false失败
	 */
	public Boolean set(String key, Object value, int expireTime);

	/**
	 * 普通缓存放入
	 * @param key 键
	 * @param value 值
	 * @param expireTime 超时时间(秒)
	 * @return true成功 false失败
	 */
	public Boolean set(String key, String value, int expireTime);

	/**
	 * 普通缓存放入
	 * @param key 键
	 * @param value 值
	 * @return true成功 false失败
	 */
	public Boolean set(String key, Object value);

	/**
	 * 存储数据
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public Boolean set(String key, String value);

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key);

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public Object getObject(String key);

	/**
	 * 递增
	 *
	 * @param key   键
	 * @param delta 要增加几(大于0)
	 * @return
	 */
	public long incr(String key, long delta) ;

	/**
	 * 递减
	 *
	 * @param key   键
	 * @param delta 要减少几(小于0)
	 * @return
	 */
	public long decr(String key, long delta);

	/**
	 * 在原有的值基础上新增字符串到末尾。
	 * @param key
	 * @param value
	 */
	public void append(String key, String value);

	/**
	 * 截取key键对应值得字符串，从开始下标位置开始到结束下标的位置(包含结束下标)的字符串。
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public String get(String key, long start, long end);

	/**
	 *  获取原来key键对应的值并重新赋新值。
	 * @param key
	 * @param value
	 * @return
	 */
	public Object getAndSet(String key, String value);

	/**
	 * 覆盖从指定位置开始的值。
	 * @param key
	 * @param value
	 * @param offset
	 */
	public void set(String key,String value,long offset);

	/**
	 *  设置map集合到redis。
	 * @param map
	 */
	public void multiSet(Map map);

	/**
	 * 根据集合取出对应的value值。
	 * @param list
	 * @return
	 */
	public List multiGet(List list);

	/**
	 *  获取指定字符串的长度。
	 * @param key
	 * @return
	 */
	public long size(String key);

	/**
	 *  如果键不存在则新增,存在则不改变已经有的值
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean setIfAbsent(String key, String value);

	/**
	 * 删除指定KEY的缓存
	 * @param keys
	 */
	public void del(String... keys);

}
