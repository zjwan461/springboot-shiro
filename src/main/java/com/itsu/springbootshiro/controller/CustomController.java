package com.itsu.springbootshiro.controller;

import com.itsu.springbootshiro.entity.User;
import com.itsu.springbootshiro.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author 苏犇
 * @date 2019/6/28 0:22
 */
@Controller
public class CustomController {

    @Resource
    private UserService userService;

    @GetMapping(value = {"/", "index"})
    public String idx() {
        return "index";
    }

    @GetMapping(value = "/login")
    public String toLogin(String kickout, String kickoutMsg, Model model) {
        if (StringUtils.isNotBlank(kickout) && kickout.equals("kickout")) {
            model.addAttribute("kickout", kickout);
            model.addAttribute("kickoutMsg", kickoutMsg);
        }
        return "login";
    }

    @PostMapping("/login.do")
    public String subToLogin(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getPassword());
        if (user.isRememberMe())
            token.setRememberMe(true);
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            throw new AuthenticationException(e);
        }
        return "redirect:/";
    }

    @RequiresRoles({"admin"})
    @GetMapping("/testRole")
    @ResponseBody
    public String testRole() {
        return "有admin角色";
    }

    @RequiresRoles({"admin1"})
    @GetMapping("/testRole1")
    @ResponseBody
    public String testRole1() {
        return "有admin1角色";
    }

}
