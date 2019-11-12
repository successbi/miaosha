package com.miaoshaproject.service;

import com.miaoshaproject.error.BuException;
import com.miaoshaproject.service.model.UserModel;

/**
 * Author:subi
 * Time:2019/11/8 14:37
 **/
public interface UserService {
    //通过用户id获取用户对象的方法
    UserModel getUserById(Integer id);

    //注册信息服务（写如数据库）
    void register(UserModel userModel) throws BuException;

    //登录校验服务
    //telephone:用户注册手机
    //password:用户加密后的密码
    UserModel validateLogin(String telephone,String encrptPassword) throws BuException;
}
