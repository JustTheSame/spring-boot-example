package com.lance.redislock.controller;


import com.lance.redislock.annotation.CacheLock;
import com.lance.redislock.annotation.CacheParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @author: zhaotian
 * @date: 2020/3/30
 */
@RestController
public class LockController {

    @CacheLock(prefix = "test")
    @GetMapping("/test")
    public String query(@CacheParam(name = "token") @RequestParam String token) {
        return "success - " + token;
    }

}
