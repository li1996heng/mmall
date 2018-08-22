package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.Result;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: Mica.Li
 * @Date: 2018/8/22 22:08
 * @Description:
 */
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;
    @Override
    public Result<String> saveOrUpdateProduct(Product product) {
        if (product == null) {
            return Result.createByErrorMessage("新增或修改参数错误");
        }else {
            if (product.getMainImage() == null) {
                if (StringUtils.isNotBlank(product.getSubImages())) {
                    String [] imgArr = product.getSubImages().split(",");
                    product.setMainImage(imgArr[0]);
                }
            }
            // 表示更新产品
            if (product.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKeySelective(product);
                if (rowCount > 0) {
                    return Result.createBySuccessMessage("更新产品成功");
                } else {
                    return Result.createByErrorMessage("更新产品失败");
                }
            }else {
                int rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                    return Result.createBySuccessMessage("新增产品成功");
                } else {
                    return Result.createByErrorMessage("新增产品失败");
                }
            }
        }
    }

    @Override
    public Result<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return Result.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        } else {
            Product product = new Product();
            product.setId(productId);
            product.setStatus(status);
            int rowCount = productMapper.updateByPrimaryKeySelective(product);
            if (rowCount > 0) {
                return Result.createBySuccess("修改销售状态成功!");
            } else{
                return Result.createBySuccess("修改销售状态失败!");
            }
        }
    }
}
