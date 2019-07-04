package com.itsu.springbootshiro.service;

import com.itsu.springbootshiro.entity.User;
import com.itsu.springbootshiro.mapper.UserMapper;
import com.itsu.springbootshiro.shiro.sessiondao.RedisSessionDao;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author 苏犇
 * @date 2019/7/3 20:59
 */

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisSessionDao redisSessionDao;

    public void handlerSessionUser(User user){
        Collection<Session> activeSessions = redisSessionDao.getActiveSessions();
        for (Session session : activeSessions) {
            Serializable oldSessionId = session.getId();
            System.out.println(oldSessionId);
        }
    }

}
