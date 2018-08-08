package com.mmall.service;

import com.mmall.common.Result;
import com.mmall.pojo.User;

/**
 * @Auther: Mica.Li
 * @Date: 2018/8/8 20:51
 * @Description: 用户登录模块业务层接口
 */
public interface IUserService {
    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    Result<User> login(String username, String password);

    /**
     * 用户注册
     * @param user
     * @return
     */
    Result<String> register(User user);
}
