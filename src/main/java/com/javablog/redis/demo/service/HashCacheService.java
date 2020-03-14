package com.javablog.redis.demo.service;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface HashCacheService extends CommonCacheService {

	/**
	 * HashGet
	 *
	 * @param key  键 不能为null
	 * @param item 项 不能为null
	 * @return 值
	 */
	public Object hget(String key, String item) ;

	/**
	 * 获取hashKey对应的所有键值
	 *
	 * @param key 键
	 * @return 对应的多个键值
	 */
	public Map <Object, Object> hmget(String key);

	/**
	 * HashSet
	 *
	 * @param key 键
	 * @param map 对应多个键值
	 * @return true 成功 false 失败
	 */
	public boolean hmset(String key, Map <String, Object> map);

	/**
	 * HashSet 并设置时间
	 *
	 * @param key  键
	 * @param map  对应多个键值
	 * @param time 时间(秒)
	 * @return true成功 false失败
	 */
	public boolean hmset(String key, Map <String, Object> map, long time);


	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 *
	 * @param key   键
	 * @param item  项
	 * @param value 值
	 * @return true 成功 false失败
	 */
	public boolean hset(String key, String item, Object value);

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 *
	 * @param key   键
	 * @param item  项
	 * @param value 值
	 * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
	 * @return true 成功 false失败
	 */
	public boolean hset(String key, String item, Object value, long time);

	/**
	 * 删除hash表中的值
	 *
	 * @param key  键 不能为null
	 * @param item 项 可以使多个 不能为null
	 */
	public void hdel(String key, Object... item);

	/**
	 * 判断hash表中是否有该项的值
	 *
	 * @param key  键 不能为null
	 * @param item 项 不能为null
	 * @return true 存在 false不存在
	 */
	public boolean hHasKey(String key, String item);

	/**
	 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
	 *
	 * @param key  键
	 * @param item 项
	 * @param by   要增加几(大于0)
	 * @return
	 */
	public long hincr(String key, String item, long by);

	/**
	 * hash递减
	 *
	 * @param key  键
	 * @param item 项
	 * @param by   要减少记(小于0)
	 * @return
	 */
	public long hdecr(String key, String item, long by);

	/**
	 * 获取指定变量中的hashMap值。
	 * @param key
	 * @return 返回LIST对象
	 */
	public List<Object> values(String key);

	/**
	 * 获取变量中的键。
	 * @param key
	 * @return
	 */
	public Set<Object>  keys(String key);

	/**
	 *  获取变量的长度。
	 * @param key
	 * @return
	 */
	public long size(String key);

	/**
	 *  以集合的方式获取变量中的值。
	 * @param key
	 * @param list
	 * @return
	 */
	public 	List multiGet(String key, List list);

	/**
	 *  如果变量值存在，在变量中可以添加不存在的的键值对，如果变量不存在，则新增一个变量，同时将键值对添加到该变量。
	 * @param key
	 * @param hashKey
	 * @param value
	 */
	public void putIfAbsent(String key, String hashKey, Object value);

	/**
	 * 匹配获取键值对，ScanOptions.NONE为获取全部键对，ScanOptions.scanOptions().match("map1").build()
	 * 匹配获取键位map1的键值对,不能模糊匹配。
	 * @param key
	 * @param options
	 * @return
	 */
	public Cursor<Map.Entry<Object,Object>> scan(String key, ScanOptions options);

	/**
	 * 删除变量中的键值对，可以传入多个参数，删除多个键值对。
	 * @param key
	 * @param hashKeys
	 */
	public void delete(String key, String... hashKeys);
}
