package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.Result;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;

/**
 * @Auther: Mica.Li
 * @Date: 2018/8/22 21:59
 * @Description: 后台商品控制层
 */
@RestController
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IProductService productService;
    @Autowired
    private IUserService userService;

    /**
     * 新增商品
     * @param session
     * @param product
     * @return
     */
    @RequestMapping("save.do")
    public Result productSave(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Result.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return productService.saveOrUpdateProduct(product);
        } else {
            return Result.createByErrorMessage("不是管理员,没有权限操作!");
        }
    }

    /**
     * 修改销售状态
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping("set_sale_status.do")
    public Result setSaleStatus(HttpSession session, Integer productId,Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Result.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return productService.setSaleStatus(productId,status);
        } else {
            return Result.createByErrorMessage("不是管理员,没有权限操作!");
        }
    }
}
