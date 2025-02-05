package uestc.config.redis;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // 存缓存
    public void set(String key, String value, Long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    // 取缓存
    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    // 清除缓存
    public void del(String key) {
        redisTemplate.delete(key);
    }
}
