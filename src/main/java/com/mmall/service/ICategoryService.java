package com.mmall.service;

import com.mmall.common.Result;
import com.mmall.pojo.Category;

/**
 * @Auther: Mica.Li
 * @Date: 2018/8/14 22:02
 * @Description:
 */
public interface ICategoryService {

    /**
     * 新增品类
     *
     * @param categoryName
     * @param parentId
     * @return
     */
    Result addCategory(String categoryName, Integer parentId);

    /**
     * 修改品类
     *
     * @param categoryId
     * @param categoryName
     * @return
     */
    Result updateCategory(Integer categoryId, String categoryName);

    /**
     * 无递归查询其子节点的品类信息
     * @param parentId
     * @return
     */
    Result getChildrenParallelCategory(Integer parentId);

    /**
     * 递归查询其子节点 (子节点的子节点的子节点.....)的品类信息
     * @param categoryId
     * @return
     */
    Result selectCategoryAndChildrenById(Integer categoryId);
}
