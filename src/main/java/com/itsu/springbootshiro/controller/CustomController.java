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
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        if (StringUtils.isNotBlank(kickout) && kickout.equals("yes")) {
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

    @GetMapping("/session/all")
    @RequiresRoles("super_admin")
    public ModelAndView toLoginUserPage() {
        Set<Map> allLoginUserInfoList = userService.getAllLoginUserInfoList();
        ModelAndView view = new ModelAndView("/loginUser");
        view.addObject("loginUsers", allLoginUserInfoList);
        return view;
    }

    @PostMapping("/kickout")
    @RequiresRoles("super_admin")
    @ResponseBody
    public Map kickout(String sessionId) {
        Map map = new HashMap();
        userService.kickout(sessionId);
        map.put("status", "200");
        map.put("message", "操作成功");
        return map;
    }
}
