package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.Result;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Set;

/**
 * @Auther: Mica.Li
 * @Date: 2018/8/14 22:04
 * @Description:
 */
@Service
public class CategoryServiceImpl implements ICategoryService{

    Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public Result addCategory(String categoryName,Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return Result.createByErrorMessage("参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return Result.createBySuccess("添加品类成功!");
        }
        return Result.createBySuccess("添加品类失败!");
    }

    @Override
    public Result updateCategory(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return Result.createByErrorMessage("参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return Result.createBySuccess("更新品类成功!");
        }
        return Result.createByErrorMessage("更新品类失败");
    }

    @Override
    public Result getChildrenParallelCategory(Integer parentId) {
        if (parentId == null) {
            return Result.createByErrorMessage("参数错误");
        }
        List<Category> categories = categoryMapper.selectCategoryChildrenByParentId(parentId);
        if (CollectionUtils.isEmpty(categories)) {
            logger.info("未找到当前分类的子分类");
        }
        return Result.createBySuccess(categories);
    }

    @Override
    public Result selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null){
            for(Category categoryItem : categorySet){
                categoryIdList.add(categoryItem.getId());
            }
        }
        return Result.createBySuccess(categoryIdList);
    }


    public Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        List<Category> categories = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category ca : categories) {
            findChildCategory(categorySet, ca.getId());
        }
        return categorySet;
    }

}
