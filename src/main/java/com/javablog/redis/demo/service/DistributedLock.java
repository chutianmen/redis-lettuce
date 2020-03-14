package com.javablog.redis.demo.service;

public interface DistributedLock {
    public static final long TIMEOUT_MILLIS = 30000;

    public static final int RETRY_TIMES = 10;

    public static final long SLEEP_MILLIS = 500;

    public boolean lock(String key);

    public boolean lock(String key, int retryTimes);

    public boolean lock(String key, int retryTimes, long sleepMillis);

    public boolean lock(String key, long expire);

    public boolean lock(String key, long expire, int retryTimes);

    public boolean lock(String key, long expire, int retryTimes, long sleepMillis);

    public boolean releaseLock(String key);
}
