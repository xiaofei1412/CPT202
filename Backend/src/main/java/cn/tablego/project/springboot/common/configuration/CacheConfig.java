package cn.tablego.project.springboot.common.configuration;


import cn.tablego.project.springboot.common.model.CaptchaEntry;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CacheConfig {

    @Bean
    public Cache<String, CaptchaEntry> captchaCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000) // 最大缓存条目数
                .build();
    }
}
