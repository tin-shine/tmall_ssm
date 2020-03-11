package com.tinshine.tmall.service;

import com.tinshine.tmall.pojo.Product;
import com.tinshine.tmall.pojo.PropertyValue;

import java.util.List;

public interface PropertyValueService {
    List<PropertyValue> list(int pid);

    void init(Product product);

    void update(PropertyValue propertyValue);

    PropertyValue get(int propertyId, int productId);
}
