package cn.tablego.project.springboot.controller;

import cn.tablego.project.springboot.common.model.AjaxResult;
import cn.tablego.project.springboot.model.Users;
import cn.tablego.project.springboot.service.UsersService;
import cn.tablego.project.springboot.service.impl.CaptchaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@RestController
public class LoginController {
    @Autowired
    private UsersService usersService;

    @Autowired
    private CaptchaService captchaService;

    @GetMapping("/captcha/generate")
    public AjaxResult generateCaptcha(@RequestParam String email) {
        String key = email + ":" + "reg";
        String captcha = captchaService.generateCaptcha(key);
        sendemail("captcha", "captcha：" + captcha, email);
        return AjaxResult.success(captcha);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public AjaxResult register(@RequestBody Users users) {
        String email = users.getEmail();
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        Users one = usersService.getOne(queryWrapper);
        if (one != null) {
            return AjaxResult.error("email is register");
        }

        String verificationCode = users.getVerificationCode();
        String key = email + ":" + "reg";
        boolean b = captchaService.validateCaptcha(key, verificationCode);
        if (!b) {
            return AjaxResult.error("captcha error");
        }

        // 设置默认角色-用户
        users.setRoleId(1);
        users.setIsActivated(true);
        users.setNickname(email);
        usersService.save(users);

        return AjaxResult.success();
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody Users users) {
        String email = users.getEmail();
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        Users one = usersService.getOne(queryWrapper);
        if (one != null) {
            Boolean isActivated = one.getIsActivated();
            if (!isActivated) {
                return AjaxResult.error("email is ban");
            }
            if (one.getPassword().equals(users.getPassword())) {
                return AjaxResult.success(one);
            } else {
                return AjaxResult.error("password error");
            }
        }
        return AjaxResult.error("email is not register");
    }

    public static boolean sendemail(String theme, String messages, String email) {
        // 配置邮件属性
        Properties prop = new Properties();
        prop.put("mail.transport.protocol", "smtp");
        prop.put("mail.host", "smtp.qq.com"); // QQ邮箱的SMTP服务器地址
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "465"); // 使用SSL加密时的端口号
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.socketFactory.port", "465");

        Session session = Session.getInstance(prop);
        session.setDebug(true);
        Message message;
        try {
            message = createSimpleMail(session, theme, messages, email);
            Transport ts = session.getTransport();
//            ts.connect("yangyimin2021@126.com", "CGNVUNXRTVADDAPC"); // 替换为你的邮箱账号和授权码
            ts.connect("1989830903@qq.com", "vsooatznrmaxcghc"); // 替换为你的邮箱账号和授权码
            ts.sendMessage(message, message.getAllRecipients());
            ts.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static MimeMessage createSimpleMail(Session session, String theme, String messages, String email) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("1989830903@qq.com")); // 替换为你的邮箱地址
        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject(theme);
        message.setText(messages);
        message.saveChanges();
        return message;
    }

}
