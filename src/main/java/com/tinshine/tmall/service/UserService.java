package com.tinshine.tmall.service;

import com.tinshine.tmall.pojo.User;

import java.util.List;

public interface UserService {
    void add(User user);
    void delete(int id);
    List<User> list();
    User get(int id);
    void update(User user);
    boolean isExist(String name);
    User get(String name, String password);
}
