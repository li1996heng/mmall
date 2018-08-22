package com.mmall.service;

import com.mmall.common.Result;
import com.mmall.pojo.Product;

/**
 * @Auther: Mica.Li
 * @Date: 2018/8/22 22:09
 * @Description:
 */
public interface IProductService {
    /**
     * 更新或新增商品
     * @param product
     * @return
     */
    Result<String> saveOrUpdateProduct(Product product);

    /**
     * 修改产品上下架
     * @param productId
     * @param status
     * @return
     */
    Result<String> setSaleStatus(Integer productId, Integer status);
}
