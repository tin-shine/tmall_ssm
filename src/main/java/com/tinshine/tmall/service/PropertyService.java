package com.tinshine.tmall.service;

import com.tinshine.tmall.pojo.Property;

import java.util.List;

public interface PropertyService {

    void add(Property property);
    void update(Property property);
    void delete(int id);
    Property get(int id);
    List list(int cid);

}
