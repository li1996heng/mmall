package com.mmall.controller;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.Result;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Auther: Mica.Li
 * @Date: 2018/8/5 23:45
 * @Description: 用户模块Controller
 */
@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    public Result<User> loging(String username, String password, HttpSession session) {
        Result<User> userResult = userService.login(username, password);
        if (userResult.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, userResult.getData());
        }
        return userResult;
    }

    /**
     * 退出登录
     * @param session
     * @return
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    public Result<String> logOut(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return Result.createBySuccess();
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    public Result<String> register(User user) {
        return userService.register(user);
    }

    /**
     * 检查用户名或邮箱是否存在
     *
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    public Result<String> checkValid(String str, String type) {
        return userService.checkValid(str, type);
    }

    /**
     * 从session中获取用户信息
     * @param session
     * @return
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    public Result<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (null != user) {
            return Result.createBySuccess(user);
        } else {
            return Result.createByErrorMessage("用户未登录,无法获取当前用户信息!");
        }
    }

    /**
     * 得到密保问题
     * @param username
     * @return
     */
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    public Result<String> forgetGetQuestion(String username) {
        return userService.selectQuestion(username);
    }

    /**
     * 校验密保答案是否输入正确
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    public Result<String> forgetCheckAnswer(String username, String question, String answer) {
        return userService.chekAnswer(username, question, answer);
    }

    /**
     * 忘记密码后修改密码
     * @param username
     * @param newPassword
     * @param forgetToken
     * @return
     */
    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    public Result<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
        return userService.forgetResetPassword(username,newPassword,forgetToken);
    }

    /**
     * 登录状态修改密码
     * @param session
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @RequestMapping(value="reset_password.do", method = RequestMethod.POST)
    public Result<String> resetPassword(HttpSession session, String oldPassword, String newPassword) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Result.createByErrorMessage("session失效,请重新登录!");
        }
        return userService.resetPassword(user, oldPassword, newPassword);
    }

    /**
     * 更新用户信息
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value="update_infomation.do", method = RequestMethod.POST)
    public Result<User> updateInformation(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return Result.createByErrorMessage("session失效,请重新登录!");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        Result<User> result = userService.updateInfomation(user);
        if (result.isSuccess()) {
            result.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Const.CURRENT_USER,result.getData());
        }
        return result;
    }

    /**
     * 从数据库中获取用户信息
     * @param session
     * @return
     */
    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    public Result<User> get_information(HttpSession session){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return Result.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录,需要强制登录status=10");
        }
        return userService.getInformation(currentUser.getId());
    }

}
