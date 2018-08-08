package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.Result;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: Mica.Li
 * @Date: 2018/8/8 20:59
 * @Description:
 */
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public Result<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0){
            return Result.createByErrorMessage("用户名不存在");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.login(username,password);
        if(user == null){
            return Result.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return Result.createBySuccess("登录成功",user);
    }

    @Override
    public Result<String> register(User user) {
        // 校验用户名是否存在
        Result<String> validResult = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResult.isSuccess()) {
            return validResult;
        }
        // 校验email是否存在
        validResult = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResult.isSuccess()) {
            return validResult;
        }
        // 设置角色
        user.setRole(Const.Role.ROLE_CUSTOMER);
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return Result.createByErrorMessage("注册失败");
        }
        return Result.createBySuccessMessage("注册成功");
    }

    public Result<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return Result.createByErrorMessage("用户名已存在");
                }
            } else if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return Result.createByErrorMessage("email已存在");
                }
            }
        }else {
            return Result.createByErrorMessage("参数错误");
        }
        return Result.createBySuccessMessage("校验成功");
    }
}
