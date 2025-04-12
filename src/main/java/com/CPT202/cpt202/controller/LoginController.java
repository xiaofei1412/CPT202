package com.CPT202.cpt202.controller;

import com.CPT202.cpt202.service.UserServiceImpl;
import com.CPT202.cpt202.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.NoSuchAlgorithmException;

@Controller
public class LoginController {

    @Autowired
    private UserServiceImpl userService;  // 注入 UserServiceImpl

    // 用户登录接口
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginUser(String usernameOrEmail, String password, Model model) throws NoSuchAlgorithmException {
        try {
            // 调用 UserService 实现登录
            User user = userService.loginUser(usernameOrEmail, password);  // 使用邮箱或昵称登录

            // 登录成功，返回首页或用户页面
            model.addAttribute("user", user);
            return "welcome";  // 登录成功后跳转的页面
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";  // 登录失败，返回登录页面
        }
    }
}
