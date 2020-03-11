package com.tinshine.tmall.service.impl;

import com.tinshine.tmall.mapper.ProductImageMapper;
import com.tinshine.tmall.pojo.ProductImage;
import com.tinshine.tmall.pojo.ProductImageExample;
import com.tinshine.tmall.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    ProductImageMapper imageMapper;

    @Override
    public void add(ProductImage image) {
        imageMapper.insert(image);
    }

    @Override
    public void delete(int id) {
        imageMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(ProductImage image) {
        imageMapper.updateByPrimaryKeySelective(image);
    }

    @Override
    public ProductImage get(int id) {
        return imageMapper.selectByPrimaryKey(id);
    }

    @Override
    public List list(int productId, String type) {
        ProductImageExample imageExample = new ProductImageExample();
        imageExample.createCriteria().andPidEqualTo(productId).andTypeEqualTo(type);
        imageExample.setOrderByClause("id desc");
        return imageMapper.selectByExample(imageExample);
    }
}