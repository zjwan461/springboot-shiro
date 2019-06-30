package com.itsu.springbootshiro;

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

}
