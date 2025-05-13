package cn.tablego.project.springboot.common.model;

import java.time.LocalDateTime;

public class CaptchaEntry {
    private final String captcha;
    private final LocalDateTime expirationTime;

    public CaptchaEntry(String captcha, LocalDateTime expirationTime) {
        this.captcha = captcha;
        this.expirationTime = expirationTime;
    }

    public String getCaptcha() {
        return captcha;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }
}
