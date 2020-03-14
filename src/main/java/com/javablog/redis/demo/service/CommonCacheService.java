package com.javablog.redis.demo.service;

public interface CommonCacheService {

	/**
	 * 删除
	 * 
	 * @param keys
	 * @return
	 */
	public void del(String... keys);

	/**
	 * 为key设置超时时间
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public boolean expire(String key, long seconds);

	/**
	 * 根据key获取过期时间
	 *
	 * @param key 键 不能为null
	 * @return 时间(秒) 返回0代表为永久有效
	 */
	public long getExpire(String key);

}
