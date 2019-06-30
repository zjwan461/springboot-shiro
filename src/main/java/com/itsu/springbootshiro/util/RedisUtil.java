package com.itsu.springbootshiro.util;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 苏犇
 * @date 2019/6/30 1:40
 */
@Component
public class RedisUtil {
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public byte[] get(String key) {
        return (byte[]) redisTemplate.opsForValue().get(key);
    }

    public void set(String key, byte[] value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void expir(String key, long time) {
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }

    public String getStr(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void setStr(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public Set<String> keys(String keyPatten) {
        Set<String> keys = stringRedisTemplate.keys(keyPatten + "*");
        return keys;
    }

    public void flushDB() {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return null;
            }
        });
    }
}
