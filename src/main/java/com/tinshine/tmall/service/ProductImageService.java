package com.tinshine.tmall.service;

import com.tinshine.tmall.pojo.ProductImage;

import java.util.List;

public interface ProductImageService {
    String SIMPLE = "type_single";
    String DETAIL = "type_detail";

    void add(ProductImage image);
    void delete(int id);
    void update(ProductImage image);
    ProductImage get(int id);
    List list(int productI, String type);
}
