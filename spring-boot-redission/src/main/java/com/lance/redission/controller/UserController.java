package com.lance.redission.controller;

import com.lance.redission.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 测试Redis的controller
 *
 * @author zhaotian
 * @date 2019/7/19 9:50
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    // 获取字符串
    @GetMapping("/get/{key}")
    public String getRedis(@PathVariable(name = "key") String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // 保存字符串
    @PostMapping("/set/{key}/{value}")
    public String getRedis(@PathVariable(name = "key") String key, @PathVariable(name = "value") String value) {
        stringRedisTemplate.opsForValue().set(key, value);
        return "SUCCESS";
    }

    // 保存对象
    @PostMapping("/postEntity")
    public String postEntity(@RequestBody User user) {
        redisTemplate.opsForValue().set(user.getUserCode(), user);
        return "SUCCESS";
    }

    // 获取对象
    @GetMapping("/getEntity/{key}")
    public User getEntity(@PathVariable(name = "key") String key) {
        return (User) redisTemplate.opsForValue().get(key);
    }

}
