package com.CPT202.cpt202.controller;

import com.CPT202.cpt202.dto.UserRegistrationRequest;
import com.CPT202.cpt202.model.User;
import com.CPT202.cpt202.service.UserServiceImpl;
import com.CPT202.cpt202.service.MailService;  // 导入 MailService
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;


import java.security.NoSuchAlgorithmException;

@Controller
public class RegisterController {

    @Autowired
    private UserServiceImpl userService;  // 注入 UserServiceImpl

    @Autowired
    private MailService mailService;  // 注入 MailService

    // 用户注册接口
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@RequestBody UserRegistrationRequest request, String inputCode, Model model) throws NoSuchAlgorithmException, MessagingException {
        try {
            // 1. 调用用户服务注册用户
            User user = userService.registerUser(request.getEmail(), request.getPassword(), request.getNickname());

            // 2. 生成验证码并发送邮件
            String generatedVerificationCode = mailService.generateVerificationCode();  // 生成验证码
            mailService.sendVerificationEmail(request.getEmail(), generatedVerificationCode);  // 发送验证码邮件

            // 3. 验证用户输入的验证码
            if (inputCode.equals(generatedVerificationCode)) {
                // 验证成功，激活用户账户
                userService.activateUser(request.getEmail());  // 激活用户方法
                model.addAttribute("message", "注册成功！账户已激活！");
                return "welcome";  // 激活成功后跳转到欢迎页面
            } else {
                model.addAttribute("error", "验证码错误！");
                return "error";  // 验证失败，返回错误页面
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";  // 出现错误时显示错误页面
        }
    }
}
