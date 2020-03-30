package com.lance.redislock;

import com.lance.redislock.common.CacheKeyGenerator;
import com.lance.redislock.common.LockKeyGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootRedislockApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootRedislockApplication.class, args);
    }

    @Bean
    public CacheKeyGenerator cacheKeyGenerator() {
        return new LockKeyGenerator();

    }

}
