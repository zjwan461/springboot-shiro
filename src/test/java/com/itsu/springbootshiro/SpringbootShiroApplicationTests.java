package com.itsu.springbootshiro;

import com.alibaba.fastjson.JSON;
import com.itsu.springbootshiro.entity.User;
import com.itsu.springbootshiro.mapper.UserMapper;
import com.itsu.springbootshiro.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootShiroApplicationTests {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserMapper userMapper;
    @Test
    public void contextLoads() {
        redisUtil.set("suben", "handsome".getBytes());
        redisUtil.setStr("xiaoai", "beautiful");
    }

    @Test
    public void redisGet() {
        String value = redisUtil.getStr("xiaoai");
        System.err.println(value);
    }

    @Test
    public void testMp() {

        User user = userMapper.getUserRolePermByUsername("suben");
        System.out.println(JSON.toJSONString(user));
        
    }
}
