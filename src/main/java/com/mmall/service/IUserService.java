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
     *
     * @param username
     * @param password
     * @return
     */
    Result<User> login(String username, String password);

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    Result<String> register(User user);

    /**
     * 检查用户名或邮箱是否存在
     *
     * @param str  检查参数
     * @param type 参数类型  username OR email
     * @return
     */
    Result<String> checkValid(String str, String type);

    /**
     * 得到密保问题
     *
     * @param username
     * @return
     */
    Result<String> selectQuestion(String username);

    /**
     * 检查密保答案是否正确
     *
     * @param username
     * @param question
     * @param anster
     * @return
     */
    Result<String> chekAnswer(String username, String question, String answer);

    /**
     * 忘记密码后修改密码
     * @param username
     * @param newPassword
     * @param token
     * @return
     */
    Result<String> forgetResetPassword(String username, String newPassword, String token);

    /**
     * 登录状态下修改密码
     * @param user
     * @param oldPassword
     * @param newPassword
     * @return
     */
    Result<String> resetPassword(User user, String oldPassword, String newPassword);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    Result<User> updateInfomation(User user);

    /**
     * 得到用户信息
     * @param id
     * @return
     */
    Result<User> getInformation(Integer id);

    /**
     * 校验登录用户是否是管理员
     * @param user
     * @return
     */
    Result checkAdminRole(User user);
}
