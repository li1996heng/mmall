package com.mmall.controller;

import com.mmall.common.Const;
import com.mmall.common.Result;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Auther: Mica.Li
 * @Date: 2018/8/5 23:45
 * @Description:
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public Result<User> loging(String username, String password, HttpSession session) {
        Result<User> userResult = userService.login(username, password);
        if (userResult.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, userResult.getData());
        }
        return userResult;
    }

    /**
     * 退出登录
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> logOut(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return Result.createBySuccess();
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    public Result<String> register(User user) {
        return userService.register(user);
    }
}
