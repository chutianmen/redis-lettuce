package com.javablog.redis.demo.service;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;

import java.util.List;
import java.util.Set;

public interface SetCacheService extends CommonCacheService {
	/**
	 * 往set中增加值
	 *
	 * @param key
	 * @param values
	 * @return
	 */
	public boolean add(String key, Object...values);

	/**
	 * 往set中增加1个值
	 *
	 * @param key
	 * @param values
	 * @param expireTime
	 * @return
	 */
	public Boolean add(String key, int expireTime, Object... values);

	/**
	 * 获取变量中的值。
	 * @param key
	 * @return
	 */
	public Set<Object> members(String key);

	/**
	 * 获取变量中值的长度。
	 * @param key
	 * @return
	 */
	public long size(String key);

	/**
	 * 检查给定的元素是否在变量中。
	 * @param key
	 * @param o
	 * @return
	 */
	public boolean isMember(String key, Object o);

	/**
	 *  转移变量的元素值到目的变量。
	 * @param key
	 * @param value
	 * @param destValue
	 * @return
	 */
	public boolean move(String key, Object value,String destValue);

	/**
	 *  弹出变量中的元素
	 * @param key
	 * @return
	 */
	public Object pop(String key);

	/**
	 *  批量移除变量中的元素。
	 * @param key
	 * @param values
	 */
	public long remove(String key, Object... values);

	/**
	 *  匹配获取键值对，ScanOptions.NONE为获取全部键值对；
	 *  ScanOptions.scanOptions().match("C").build()匹配获取键位map1的键值对,不能模糊匹配。
	 * @param key
	 * @param options
	 * @return
	 */
	public Cursor<Object> scan(String key, ScanOptions options);

	/**
	 * 通过集合求差值
	 * @param key
	 * @param list
	 */
	public Set<Object> difference(String key, List list);

	/**
	 * 通过给定的key求2个set变量的差值。
	 * @param key
	 * @param otherKeys
	 */
	public Set<Object> difference(String key, String otherKeys);

	/**
	 * 将求出来的差值元素保存。
	 * @param key
	 * @param otherKey
	 * @param destKey
	 */
	public void differenceAndStore(String key, String otherKey, String destKey);

	/**
	 * 将求出来的差值元素保存。
	 * @param key
	 * @param otherKeys
	 * @param destKey
	 */
	public void differenceAndStore(String key, List<Object> otherKeys, String destKey);

	/**
	 * 获取去重的随机元素。
	 * @param key
	 * @param count
	 * @return
	 */
	public Set<Object> distinctRandomMembers(String key, long count);

	/**
	 * 获取2个变量中的交集。
	 * @param key
	 * @param otherKey
	 * @return
	 */
	public Set<Object> intersect(String key, String otherKey);

	/**
	 *  获取2个变量交集后保存到最后一个参数上。
	 * @param key
	 * @param otherKey
	 * @param destKey
	 */
	public void intersectAndStore(String key, String otherKey, String destKey);

	/**
	 *  获取2个变量交集后保存到最后一个参数上。
	 * @param key
	 * @param otherKey
	 * @param destKey
	 */
	public void intersectAndStore(String key, List otherKey, String destKey);

	/**
	 * 获取2个变量的合集。
	 * @param key
	 * @param otherKey
	 * @return
	 */
	public Set<Object> union(String key, String otherKey);

	/**
	 * 获取多个变量的合集。
	 * @param key
	 * @param list 多个变量LIST
	 * @return 返回合集
	 */
	public Set<Object> union(String key, List list);

	/**
	 * 获取2个变量合集后保存到最后一个参数上。
	 * @param key
	 * @param otherKey
	 * @param destKey
	 * @return
	 */
	public void unionAndStore(String key, String otherKey, String destKey);

	/**
	 * 获取多个变量的合集并保存到最后一个参数上
	 * @param key
	 * @param list
	 * @param destKey
	 */
	public void unionAndStore(String key, List list, String destKey);

	/**
	 * 随机获取变量中的元素。
	 * @param key
	 * @return
	 */
	public Object randomMember(String key);

	/**
	 * 随机获取变量中指定个数的元素。
	 * @param key
	 * @param count
	 * @return
	 */
	public List<Object> randomMembers(String key, long count);

	/**
	 * 获取多个变量之间的交集。
 	 * @param key
	 * @param list
	 * @return
	 */
	public Set<Object> intersect(String key, List list);
}
