package com.lance.redission.token;

import java.lang.annotation.*;

/**
 * 防重复提交注解
 *
 * @author zhaotian
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenVerify {
    String value() default "";
}
