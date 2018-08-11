package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.Result;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Auther: Mica.Li
 * @Date: 2018/8/8 20:59
 * @Description:
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return Result.createByErrorMessage("用户名不存在");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.login(username, md5Password);
        if (user == null) {
            return Result.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return Result.createBySuccess("登录成功", user);
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
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return Result.createByErrorMessage("注册失败");
        }
        return Result.createBySuccessMessage("注册成功");
    }

    @Override
    public Result<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return Result.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return Result.createByErrorMessage("email已存在");
                }
            }
        } else {
            return Result.createByErrorMessage("参数错误");
        }
        return Result.createBySuccessMessage("校验成功");
    }

    @Override
    public Result<String> selectQuestion(String username) {
        Result<String> validResult = this.checkValid(username, Const.USERNAME);
        if (validResult.isSuccess()) {
            return Result.createByErrorMessage("用户名不存在");
        }
        String question = userMapper.selectQuestion(username);
        if (StringUtils.isNotBlank(question)) {
            return Result.createBySuccess(question);
        }
        return Result.createByErrorMessage("密保问题是空的");
    }

    @Override
    public Result<String> chekAnswer(String username, String question, String answer) {
        int resultCount = userMapper.chekAnswer(username, question, answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return Result.createBySuccess(forgetToken);
        }
        return Result.createByErrorMessage("密保答案输入错误!");
    }

    @Override
    public Result<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
        Result<String> validResult = this.checkValid(username, Const.USERNAME);
        if (validResult.isSuccess()) {
            return Result.createByErrorMessage("用户名不存在");
        }
        // 前台未传入token
        if (StringUtils.isBlank(forgetToken)) {
            return Result.createByErrorMessage("token不存在");
        }
        // 得到本地缓存的token
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return Result.createByErrorMessage("token无效或者过期");
        }
        String md5NewPassword = MD5Util.MD5EncodeUtf8(newPassword);
        // 比较前台传入的token和本地缓存的token是否一致
        if (StringUtils.equals(forgetToken, token)) {
            int rowCount = userMapper.updatePassword(username, md5NewPassword);
            if (rowCount > 0) {
                return Result.createBySuccess("密码修改成功!");
            }
        } else {
            return Result.createByErrorMessage("token错误,请重新获取重置密码的token");
        }
        return Result.createByErrorMessage("密码修改失败!");
    }

    @Override
    public Result<String> resetPassword(User user, String oldPassword, String newPassword) {
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword), user.getId());
        System.out.println(resultCount);
        if (resultCount == 0) {
            return Result.createByErrorMessage("原始密码输入错误!");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        int rowCount = userMapper.updateByPrimaryKeySelective(user);
        if (rowCount > 0) {
            return Result.createBySuccess("密码修改成功!");
        }
        return Result.createByErrorMessage("密码修改失败!");
    }

    @Override
    public Result<User> updateInfomation(User user) {
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return Result.createByErrorMessage("邮箱被占用!");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return Result.createBySuccess("用户信息更新成功", updateUser);
        }
        return Result.createByErrorMessage("用户信息更新失败!");
    }

    @Override
    public Result<User> getInformation(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user != null) {
            user.setPassword(StringUtils.EMPTY);
            return Result.createBySuccess(user);
        }
        return Result.createByErrorMessage("未查到用户信息");
    }
}
