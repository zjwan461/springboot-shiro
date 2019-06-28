package com.itsu.springbootshiro.controller;

import com.itsu.springbootshiro.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 苏犇
 * @date 2019/6/28 0:22
 */
@Controller
public class CustomController {

    @GetMapping(value = {"/", "index"})
    public String idx() {
        return "index";
    }

    @GetMapping(value = "/login")
    public String toLogin() {
        return "login";
    }

    @PostMapping("/login.do")
    @ResponseBody
    public String subToLogin(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getPassword());
        if (user.isRememberMe())
            token.setRememberMe(true);
        subject.login(token);
        return "登陆成功";
    }

    @RequiresRoles({"admin1"})
    @GetMapping("/testRole")
    @ResponseBody
    public String testRole() {
        return "有admin角色";
    }
}
