package com.tinshine.tmall.service;

import com.tinshine.tmall.pojo.Order;
import com.tinshine.tmall.pojo.OrderItem;

import java.util.List;

public interface OrderService {

    String WAIT_CONFIRM = "等待确认中";
    String WAIT_PAY = "等待付款中";
    String WAIT_DELIVERY = "等待发货中";
    String DELETE = "已删除";
    String WAIT_REVIEW = "等待评论";
    String FINISHED = "订单已完成";

    void add(Order order);
    void delete(int id);
    Order get(int id);
    List<Order> list();
    void update(Order order);
    float add(Order order, List<OrderItem> orderItems);
    List<Order> list(int uid, String excludedStatus);
}
