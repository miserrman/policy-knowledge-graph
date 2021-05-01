package com.longyan.policy.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RedisDao {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void saveDataById(String id, String object) {
        Integer expireTime = 3;
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(id, object, expireTime, TimeUnit.DAYS);
    }

    public String getSaveObject(String id) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(id);
    }

}
