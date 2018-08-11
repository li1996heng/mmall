package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.Result;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Auther: Mica.Li
 * @Date: 2018/8/11 16:46
 * @Description:
 */
@RestController
@RequestMapping("/manage/user")
public class UserManageController {
    @Autowired
    private IUserService userService;

    @RequestMapping(value="login.do",method = RequestMethod.POST)
    public Result<User> login(String username, String password, HttpSession session){
        Result<User> response = userService.login(username,password);
        if(response.isSuccess()){
            User user = response.getData();
            if(user.getRole() == Const.Role.ROLE_ADMIN){
                //说明登录的是管理员
                session.setAttribute(Const.CURRENT_USER,user);
                return response;
            }else{
                return Result.createByErrorMessage("不是管理员,无法登录");
            }
        }
        return response;
    }
}
