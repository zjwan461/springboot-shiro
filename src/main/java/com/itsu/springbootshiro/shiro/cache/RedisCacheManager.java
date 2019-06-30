package com.itsu.springbootshiro.shiro.cache;

import com.itsu.springbootshiro.util.RedisUtil;
import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import javax.annotation.Resource;

/**
 * @author 苏犇
 * @date 2019/6/30 15:33
 */

public class RedisCacheManager extends AbstractCacheManager {

    @Resource
    private RedisUtil redisUtil;

    @Override
    protected Cache createCache(String name) throws CacheException {
        return new RedisCache(name, redisUtil);
    }
}
