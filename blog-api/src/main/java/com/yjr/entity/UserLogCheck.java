package com.yjr.entity;

import lombok.Data;

/**
 * @program: blog-api
 * @description: 用户登录验证
 * @author: Yjr
 * @create: 2019-08-10 14:55
 **/
@Data
public class UserLogCheck {

    private String  account;

    private String password;

    private String code;


}
