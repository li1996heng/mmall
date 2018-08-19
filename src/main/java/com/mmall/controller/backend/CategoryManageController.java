package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.Result;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Auther: Mica.Li
 * @Date: 2018/8/14 21:49
 * @Description:
 */
@RestController
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ICategoryService categoryService;

    /**
     * 新增类别
     *
     * @param session
     * @param parentId
     * @param categoryName
     * @return
     */
    @RequestMapping(value = "add_category.do")
    public Result<String> addCategory(HttpSession session, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId, String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Result.createByErrorMessage("请登录后再操作!");
        }
        // 是否是管理员
        if (userService.checkAdminRole(user).isSuccess()) {
            return categoryService.addCategory(categoryName, parentId);
        } else {
            return Result.createByErrorMessage("权限不足,无法操作!");
        }
    }

    /**
     * 更新品类名称
     * @param session
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping(value = "update_category_name.do")
    public Result<String> setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Result.createByErrorMessage("请登录后再操作");
        }
        // 是否是管理员
        if (userService.checkAdminRole(user).isSuccess()) {
            return categoryService.updateCategory(categoryId, categoryName);
        } else {
            return Result.createByErrorMessage("权限不足,无法操作!");
        }
    }

    /**
     * 无递归查询其子节点的品类信息
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping("get_category.do")
    public Result getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Result.createByErrorMessage("请登录后再操作");
        }
        // 是否是管理员
        if (userService.checkAdminRole(user).isSuccess()) {
            return categoryService.getChildrenParallelCategory(categoryId);
        } else {
            return Result.createByErrorMessage("权限不足,无法操作!");
        }
    }

    /**
     * 递归查询其子节点 (子节点的子节点的子节点.....)的品类信息
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping("get_deep_category.do")
    public Result getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Result.createByErrorMessage("请登录后再操作");
        }
        // 是否是管理员
        if (userService.checkAdminRole(user).isSuccess()) {
            return categoryService.selectCategoryAndChildrenById(categoryId);
        } else {
            return Result.createByErrorMessage("权限不足,无法操作!");
        }
    }

}
