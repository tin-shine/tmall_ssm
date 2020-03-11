package com.tinshine.tmall.service.impl;

import com.tinshine.tmall.mapper.ProductMapper;
import com.tinshine.tmall.pojo.Category;
import com.tinshine.tmall.pojo.Product;
import com.tinshine.tmall.pojo.ProductExample;
import com.tinshine.tmall.pojo.ProductImage;
import com.tinshine.tmall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductImageService productImageService;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    ReviewService reviewService;

    @Override
    public void add(Product product) {
        productMapper.insert(product);
    }

    @Override
    public void delete(int id) {
        productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Product product) {
        productMapper.updateByPrimaryKeySelective(product);
    }

    @Override
    public Product get(int id) {
        Product product = productMapper.selectByPrimaryKey(id);
        Category category = categoryService.get(product.getCid());
        product.setCategory(category);
        setDefaultImage(product);
        return product;
    }

    @Override
    public List<Product> list(int cid) {
        ProductExample example = new ProductExample();
        example.createCriteria().andCidEqualTo(cid);
        example.setOrderByClause("id desc");
        List<Product> products = productMapper.selectByExample(example);
        for (Product product: products) {
            product.setCategory(categoryService.get(product.getCid()));
            setDefaultImage(product);
        }
        return products;
    }

    @Override
    public void setDefaultImage(Product product) {
        List<ProductImage> imageList = productImageService.list(product.getId(), ProductImageService.SIMPLE);
        if (!imageList.isEmpty()) {
            product.setDefaultProductImage(imageList.get(0));
        }
    }

    @Override
    public void fill(Category category) {
        category.setProducts(list(category.getId()));
    }

    @Override
    public void fillByRow(List<Category> categories) {
        int productNumberPerRow = 8;
        for (Category category : categories) {
            List<Product> products = category.getProducts();
            List<List<Product>> productsByRow = new ArrayList<>();
            for (int i = 0; i < products.size(); i += productNumberPerRow) {
                int size = i + productNumberPerRow;
                size = Math.min(size, products.size());
                productsByRow.add(products.subList(i, size));
            }
            category.setProductsByRow(productsByRow);
        }
    }

    @Override
    public void setSaleAndReviewNumber(Product product) {
        product.setSaleCount(orderItemService.getSaleCount(product.getId()));
        product.setReviewCount(reviewService.getCount(product.getId()));
    }

    @Override
    public List<Product> search(String keyword) {
        ProductExample example = new ProductExample();
        example.createCriteria().andNameLike("%" + keyword + "%");
        List<Product> products = productMapper.selectByExample(example);
        for (Product product : products) {
            setDefaultImage(product);
            product.setCategory(categoryService.get(product.getId()));
            setSaleAndReviewNumber(product);
        }
        return products;
    }
}
