package me.junbin.gradprj.service.impl;

import me.junbin.gradprj.service.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/22 14:46
 * @description : 其中 Key 表示 {@link me.junbin.gradprj.domain.Document#id}，
 * Value 表示 {@link me.junbin.gradprj.domain.Account#id}
 */
@Service("documentEditingService")
public class DocumentEditingService implements RedisCacheService<String, String> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean putIfAbsent(String key, String value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    @Override
    public boolean putIfAbsent(String key, String value, long timeout, TimeUnit unit) {
        boolean setSuccess = redisTemplate.opsForValue().setIfAbsent(key, value);
        if (setSuccess) {
            redisTemplate.expire(key, timeout, unit);
        }
        return setSuccess;
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void remove(String... keys) {
        if (keys != null) {
            List<String> keyList = new ArrayList<>(Arrays.asList(keys));
            redisTemplate.delete(keyList);
        }
    }

    @Override
    public void remove(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    @Override
    public void put(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void put(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public String get(String key) {
        Object val = redisTemplate.opsForValue().get(key);
        return val == null ? null : val.toString();
    }

}
