package com.itsu.springbootshiro.shiro.realm;

import com.itsu.springbootshiro.entity.Perm;
import com.itsu.springbootshiro.entity.User;
import com.itsu.springbootshiro.mapper.UserMapper;
import com.itsu.springbootshiro.util.ByteSourceUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 苏犇
 * @date 2019/6/27 23:26
 */

public class CustomRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(CustomRealm.class);

    @Resource
    private UserMapper userMapper;

    /**
     * 授权
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String userName = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(getRolesByUserName(userName));
        authorizationInfo.setStringPermissions(getPermissionsByUserName(userName));
        return authorizationInfo;
    }

    private Set<String> getPermissionsByUserName(String userName) {
        Set<String> perms = new HashSet<>();
        List<Perm> permsFromDB = getUserByUserName(userName).getRole().getPerms();
        permsFromDB.forEach(perm -> {
            perms.add(perm.getPermName());
        });
        return perms;
    }

    private Set<String> getRolesByUserName(String userName) {

        Set<String> roles = new HashSet<>();
        String roleName = getUserByUserName(userName).getRole().getRoleName();
        roles.add(roleName);
        return roles;
    }


    /**
     * 认证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName = (String) token.getPrincipal();
        User user = getUserByUserName(userName);
        if (user == null) {
            throw new AuthenticationException("用户名或密码错误");
        }
        if (!user.getStatus().equals("0")) {
            throw new AuthenticationException("账号已被锁定");
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName, user.getPassword(), ByteSourceUtil.bytes(user.getUserName()), getName());
        return authenticationInfo;
    }

    private User getUserByUserName(String userName) {
        User user = userMapper.getUserRolePermByUsername(userName);
        return user;
    }


}
