package com.itsu.springbootshiro.service;

import com.itsu.springbootshiro.entity.User;
import com.itsu.springbootshiro.mapper.UserMapper;
import com.itsu.springbootshiro.shiro.session.RedisSessionDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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

    public Set<Map> getAllLoginUserInfoList() {
        Collection<Session> activeSessions = redisSessionDao.getActiveSessions();
        Set<Map> loginUserInfo = new HashSet<>();
        activeSessions.stream().filter(session -> {
            if (session.getAttribute("loginUser") != null && SecurityUtils.getSubject().isAuthenticated())
                return true;
            return false;
        }).forEach(session -> {
            Map map = new HashMap();
            User loginUser = (User) session.getAttribute("loginUser");
            map.put("userName", loginUser.getUserName());
            map.put("role", loginUser.getRole().getRoleName());
            map.put("sessionId", session.getId());
            map.put("login_time", session.getStartTimestamp());
            loginUserInfo.add(map);
        });

        return loginUserInfo;
    }


    public void kickout(String sessionId) {
        Collection<Session> activeSessions = redisSessionDao.getActiveSessions();
        for (Session session : activeSessions) {
            if (session.getId().equals(sessionId)) {
                session.setAttribute("kickout", "yes");
                session.setAttribute("kickoutMsg", "您被管理员踢下线");
                redisSessionDao.update(session);
            }
        }


    }
}
