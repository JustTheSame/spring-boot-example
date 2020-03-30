package com.lance.redission.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhaotian
 * @date: 2020/3/30
 */
@Data
public class User implements Serializable {
    // 主键
    private Integer id;
    // 用户code
    private String userCode;
    // 姓名
    private String name;
    // 年龄
    private Integer age;
}
