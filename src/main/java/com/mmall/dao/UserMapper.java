package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 检查用户名是否存在
     * @param username
     * @return
     */
    int checkUsername(String username);

    /**
     * 检查邮箱是否存在
     * @param email
     * @return
     */
    int checkEmail(String email);

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    User login(@Param("username") String username, @Param("password") String password);

    /**
     * 查询密保问题
     * @param username
     * @return
     */
    String selectQuestion(String username);

    /**
     * 检查密保答案是否正确
     * @param username
     * @param question
     * @param anster
     * @return
     */
    int chekAnswer(@Param("username") String username,@Param("question")String question,@Param("answer")String answer);

    /**
     * 修改密码
     * @param username
     * @param newPassword
     * @return
     */
    int updatePassword(@Param("username") String username,@Param("newPassword") String newPassword);

    /**
     * 修改密码前检查用户输入的旧密码是否正确
     * @param oldPassword
     * @param userId
     * @return
     */
    int checkPassword(@Param("oldPassword")String oldPassword,@Param("userId")Integer userId);

    /**
     * 更新用户信息时检查邮箱是否被占用
     * @param email
     * @param userId
     * @return
     */
    int checkEmailByUserId(@Param("email")String email,@Param("userId")Integer userId);
}