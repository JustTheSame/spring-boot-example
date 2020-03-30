package com.lance.redission.token;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 生成token和删除token
 *
 * @author zhaotian
 */
@Slf4j
@Component
public class ActionToken {

    // 默认缓存时间
    private final Long TOKEN_EXPIRE_TIME = 60 * 60 * 24L;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /*
     * 生成token
     */
    public String createToken(String sessionId) {
        if (StringUtils.isBlank(sessionId)) {
            return null;
        }

        // 使用UUID当token
        String token = UUID.randomUUID().toString();
        // 存入缓存并设置有效期 TimeUnit.SECONDS 单位：秒
        stringRedisTemplate.opsForValue().set(token, sessionId, TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

        if (StringUtils.isBlank(stringRedisTemplate.opsForValue().get(token))) {
            throw new RuntimeException("生成token缓存redis失败");
        }

        return token;
    }

    /*
     * 校验token
     */
    public String tokenVerify(String token) {
        if (StringUtils.isBlank(token)) {
            log.info("token 为空");
            return "请勿重复提交";
        }

        String sessionId = stringRedisTemplate.opsForValue().get(token);
        if (StringUtils.isBlank(sessionId)) {
            log.info("Redis 中 key 为 token 的不存在");
            return "请勿重复提交";
        }

        // token 存在，移除Redis中的token，进入业务逻辑
        stringRedisTemplate.delete(token);
        log.info("redis 删除key为token:[{}]成功，进入业务逻辑", token);

        return "";
    }
}
