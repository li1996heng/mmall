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
}