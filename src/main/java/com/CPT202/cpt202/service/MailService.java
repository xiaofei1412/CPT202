package com.CPT202.cpt202.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    // 发送验证码邮件
    public void sendVerificationEmail(String toEmail, String verificationCode) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("邮箱验证");
        helper.setText("您的验证码是：" + verificationCode);

        // 发送邮件
        javaMailSender.send(message);
    }
}
