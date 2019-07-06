package com.itsu.springbootshiro.shiro.cache;

import com.itsu.springbootshiro.util.RedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.util.SerializationUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 苏犇
 * @date 2019/6/30 15:36
 */

public class RedisCache<K, V> implements Cache<K, V> {

    private String cacheName;

    private RedisUtil redisUtil;

    public static final String CACHE_PREFIX = "shiro:cache:";

    public RedisCache() {
    }

    public String getKey(K key) {
        return CACHE_PREFIX + this.cacheName + ":" + key.toString();
    }

    public RedisCache(String cacheName, RedisUtil redisUtil) {
        this.cacheName = cacheName;
        this.redisUtil = redisUtil;
    }


    @Override
    public V get(K key) throws CacheException {
        byte[] bytes = redisUtil.get(getKey(key));
        if (bytes != null) {
            return (V) SerializationUtils.deserialize(bytes);
        }
        return null;
    }

    @Override
    public V put(K key, V value) throws CacheException {
        String strKey = getKey(key);
        byte[] byteValue = SerializationUtils.serialize(value);
        redisUtil.set(strKey, byteValue);
        redisUtil.expir(strKey, 1800);
        return value;
    }

    @Override
    public V remove(K key) throws CacheException {
        String strKey = getKey(key);
        byte[] byteValue = redisUtil.get(strKey);
        redisUtil.del(strKey);
        if (byteValue != null) {
            return (V) SerializationUtils.deserialize(byteValue);
        }
        return null;
    }

    @Override
    public void clear() throws CacheException {
        Set<String> keys = redisUtil.keys("shiro:cache:*");
        keys.forEach(key -> redisUtil.del(key));

    }

    @Override
    public int size() {
        return redisUtil.keys("shiro:cache:*").size();
    }

    @Override
    public Set<K> keys() {
        return (Set<K>) redisUtil.keys("shiro:cache:*");
    }

    @Override
    public Collection<V> values() {
        Set<String> keys = redisUtil.keys("shiro:cache:*");
        Set<V> sessions = new HashSet<>();
        keys.forEach(key -> {
            byte[] valueByt = redisUtil.get(key);
            sessions.add((V) SerializationUtils.deserialize(valueByt));
        });
        return sessions;
    }
}
