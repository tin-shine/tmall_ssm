package com.tinshine.tmall.service;

import com.tinshine.tmall.pojo.Order;
import com.tinshine.tmall.pojo.OrderItem;

import java.util.List;

public interface OrderItemService {
    void add(OrderItem orderItem);
    void delete(int id);
    OrderItem get(int id);
    void update(OrderItem orderItem);
    List list();
    void fill(Order order);
    int getSaleCount(int pid);
    List<OrderItem> listByUser(int uid);
}
