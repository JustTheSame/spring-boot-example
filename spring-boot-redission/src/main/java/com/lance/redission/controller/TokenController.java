package com.lance.redission.controller;

import com.lance.redission.token.ActionToken;
import com.lance.redission.token.TokenVerify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaotian
 * @date 2019/7/23 16:28
 */
@Slf4j
@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private ActionToken actionToken;

    @RequestMapping("/createToken")
    public String createToke() {
        String sessionId = "123456";
        return actionToken.createToken(sessionId);
    }

    @TokenVerify
    @RequestMapping("/test")
    public void testToken() {
        log.info("正常业务逻辑");
    }
}
