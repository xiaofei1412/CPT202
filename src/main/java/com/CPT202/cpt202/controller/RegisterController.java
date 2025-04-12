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

import java.security.NoSuchAlgorithmException;

@Controller
public class RegisterController {

    @Autowired
    private UserServiceImpl userService;  // 注入 UserServiceImpl

    @Autowired
    private MailService mailService;  // 注入 MailService

    // 用户注册接口
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(UserRegistrationRequest request, Model model) throws NoSuchAlgorithmException, MessagingException {
        try {
            // 1. 调用用户服务注册用户
            User user = userService.registerUser(request.getEmail(), request.getPassword(), request.getNickname());

            // 2. 生成验证码
            String verificationCode = generateVerificationCode();

            // 3. 发送验证码邮件!!
            mailService.sendVerificationEmail(request.getEmail(), verificationCode);

            // 4. 注册成功
            model.addAttribute("message", "注册成功！请检查您的邮箱以获取验证码。");
            model.addAttribute("user", user);  // 将用户信息传递给视图
            return "success";  // 注册成功后显示成功页面
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";  // 出现错误时显示错误页面
        }
    }

    // 生成验证码
    private String generateVerificationCode() {
        return String.format("%06d", (int)(Math.random() * 1000000));  // 生成6位验证码
    }

}

