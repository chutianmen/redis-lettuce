package com.javablog.redis.demo.service;

import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Set;

public interface ZSetCacheService extends CommonCacheService {
    public boolean add(String key, Object value, double score);

    public Set<Object> range(String key, long start, long end);

    public Set<Object> rangeByLex(String key, RedisZSetCommands.Range range);

    public long zCard(String key);

    public long count(String key, double min, double max);

    public double incrementScore(String key, Object value, double delta);

    public double score(String key, Object o);

    public Set<Object> rangeByLex(String key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit);

    public void add(String key, Set<ZSetOperations.TypedTuple<Object>> tuples);

    public Set<Object> rangeByScore(String key, double min, double max);

    public Set<Object> rangeByScore(String key, double min, double max, long offset, long count);

    public Set<ZSetOperations.TypedTuple<Object>> rangeWithScores(String key, long start, long end);

    public Set<ZSetOperations.TypedTuple<Object>> rangeByScoreWithScores(String key, double min, double max);

    public Set<ZSetOperations.TypedTuple<Object>> rangeByScoreWithScores(String key, double min, double max, long offset, long count);

    public long rank(String key, Object o);

    public Cursor<ZSetOperations.TypedTuple<Object>> scan(String key, ScanOptions options);

    public Set<Object> reverseRange(String key, long start, long end);

    public Set<Object> reverseRangeByScore(String key, double min, double max);

    public Set<Object> reverseRangeByScore(String key, double min, double max, long offset, long count);

    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeByScoreWithScores(String key, double min, double max);

    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeByScoreWithScores(String key, double min, double max, long offset, long count);

    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScores(String key, long start, long end);

    public long reverseRank(String key, Object o);

    public long intersectAndStore(String key, String otherKey, String destKey);

    public long intersectAndStore(String key, List list, String destKey);

    public long unionAndStore(String key, String otherKey, String destKey);

    public long unionAndStore(String key,  List list, String destKey);

    public long remove(String key, Object... values);

    public long removeRangeByScore(String key, double min, double max);

    public long removeRange(String key, long start, long end);
}