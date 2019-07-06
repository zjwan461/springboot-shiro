package com.itsu.springbootshiro.shiro.filter;

import com.itsu.springbootshiro.entity.User;
import com.itsu.springbootshiro.mapper.UserMapper;
import com.itsu.springbootshiro.shiro.sessiondao.RedisSessionDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 苏犇
 * @date 2019/7/3 21:08
 */

public class UserSessionFilter extends AccessControlFilter {

    @Resource
    private RedisSessionDao redisSessionDao;

    @Resource
    private UserMapper userMapper;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);

        HttpSession session = WebUtils.toHttp(request).getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        String userName = (String) subject.getPrincipal();
        if (loginUser == null && subject.isAuthenticated()) {
            User user = userMapper.getUserRolePermByUsername(userName);
            session.setAttribute("loginUser", user);
        }

        String kickout = (String) session.getAttribute("kickout");
        boolean kickoutFlag = StringUtils.isBlank(kickout) ? false : true;

        if (kickoutFlag) {

            return false;
        }

        if (subject.isAuthenticated()) {
            Collection<Session> activeSessions = redisSessionDao.getActiveSessions();
            for (Session s : activeSessions) {
                User u1 = (User) s.getAttribute("loginUser");
                User u2 = (User) session.getAttribute("loginUser");
                if (u1 != null && u2 != null && u1.getUserName().equals(u2.getUserName()) && !s.getId().equals(session.getId())) {
                    s.setAttribute("kickout", "yes");
                    s.setAttribute("kickoutMsg", "您的账号在别处登陆，你被迫下线。IP=" + request.getRemoteAddr());
                    redisSessionDao.update(s);
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        Subject subject = super.getSubject(request, response);
        Session session = subject.getSession();
        String kickout = (String) session.getAttribute("kickout");
        String kickoutMsg = (String) session.getAttribute("kickoutMsg");
        Map params = new HashMap();
        params.put("kickout", kickout);
        params.put("kickoutMsg", kickoutMsg);
        WebUtils.issueRedirect(request, response, "/login", params);
        subject.logout();
        return false;
    }
}
