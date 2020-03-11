package com.tinshine.tmall.service.impl;

import com.tinshine.tmall.mapper.OrderItemMapper;
import com.tinshine.tmall.mapper.OrderMapper;
import com.tinshine.tmall.pojo.Order;
import com.tinshine.tmall.pojo.OrderExample;
import com.tinshine.tmall.pojo.OrderItem;
import com.tinshine.tmall.pojo.OrderItemExample;
import com.tinshine.tmall.service.OrderItemService;
import com.tinshine.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    ProductService productService;

    @Autowired
    OrderMapper orderMapper;

    @Override
    public void add(OrderItem orderItem) {
        orderItemMapper.insert(orderItem);
    }

    @Override
    public void delete(int id) {
        orderItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public OrderItem get(int id) {
        return orderItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(OrderItem orderItem) {
        orderItemMapper.updateByPrimaryKeySelective(orderItem);
    }

    @Override
    public List list() {
        OrderItemExample example = new OrderItemExample();
        example.setOrderByClause("id desc");
        return orderItemMapper.selectByExample(example);
    }

    @Override
    public void fill(Order order) {
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andOidEqualTo(order.getId());
        example.setOrderByClause("id desc");
        List<OrderItem> orderItems = orderItemMapper.selectByExample(example);
        float price = 0;
        int count = 0;
        for (OrderItem orderItem : orderItems) {
            orderItem.setProduct(productService.get(orderItem.getPid()));
            price += orderItem.getNumber() * orderItem.getProduct().getPromotePrice();
            count += orderItem.getNumber();
        }
        order.setPrice(price);
        order.setCount(count);
        order.setOrderItems(orderItems);
    }

    @Override
    public int getSaleCount(int pid) {
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andPidEqualTo(pid);
        List<OrderItem> orderItems = orderItemMapper.selectByExample(example);
        int ret = 0;
        for (OrderItem orderItem : orderItems) {
            ret += orderItem.getNumber();
        }
        return ret;
    }

    @Override
    public List<OrderItem> listByUser(int uid) {
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andUidEqualTo(uid);
        example.setOrderByClause("id desc");
        List<OrderItem> orderItems = orderItemMapper.selectByExample(example);
        for (OrderItem orderItem : orderItems) {
            orderItem.setProduct(productService.get(orderItem.getPid()));
        }
        return orderItems;
    }
}
