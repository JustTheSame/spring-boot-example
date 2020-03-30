package com.lance.redission.controller;


import com.lance.redission.redis.DistributedRedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分布式Redis锁测试controller
 *
 * @author zhaotian
 * @date 2019/7/19 17:30
 */
@RestController
@RequestMapping("/lock")
public class LockTestController {

    @Autowired
    private DistributedRedisLock distributedRedisLock;

    // 测试分布式锁
    @GetMapping("/testLock")
    public void testLock() {
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Boolean lockFlag = distributedRedisLock.lock("LOCK");
                }
            }).start();
        }
    }

}
