package com.tinshine.tmall.service;

import com.tinshine.tmall.pojo.Category;
import com.tinshine.tmall.pojo.Product;

import java.util.List;

public interface ProductService {
    void add(Product product);
    void delete(int id);
    void update(Product product);
    Product get(int id);
    List list(int cid);
    void setDefaultImage(Product product);
    void fill(Category category);
    void fillByRow(List<Category> categories);
    void setSaleAndReviewNumber(Product product);
    List<Product> search(String keyword);
}
