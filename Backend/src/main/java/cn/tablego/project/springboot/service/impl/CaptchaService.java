package cn.tablego.project.springboot.service.impl;
import cn.tablego.project.springboot.common.model.CaptchaEntry;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class CaptchaService {

    private final SecureRandom random = new SecureRandom();


    private final Cache<String, CaptchaEntry> captchaCache;

    @Autowired
    public CaptchaService(Cache<String, CaptchaEntry> captchaCache) {
        this.captchaCache = captchaCache;
    }

    public String generateCaptcha(String key) {
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(36); // 生成0-9和a-z之间的字符
            char c = (char) (index < 10 ? '0' + index : 'a' + index - 10);
            captcha.append(c);
        }
        LocalDateTime expirationTime = LocalDateTime.now(ZoneId.systemDefault()).plusMinutes(5); // 设置验证码有效期为5分钟
        captchaCache.put(key, new CaptchaEntry(captcha.toString(), expirationTime));
        return captcha.toString();
    }

    public boolean validateCaptcha(String key, String input) {
        CaptchaEntry entry = captchaCache.getIfPresent(key);
        if (entry != null && !LocalDateTime.now(ZoneId.systemDefault()).isAfter(entry.getExpirationTime())) {
            if (entry.getCaptcha().equals(input)) {
                captchaCache.invalidate(key); // 如果验证码匹配，则清除缓存
                return true;
            }
        }
        return false;
    }
}
