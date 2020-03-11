package com.tinshine.tmall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tinshine.tmall.pojo.Order;
import com.tinshine.tmall.service.OrderItemService;
import com.tinshine.tmall.service.OrderService;
import com.tinshine.tmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderItemService orderItemService;

    @RequestMapping("admin_order_list")
    public String list(Model model, Page page) {
        PageHelper.offsetPage(page.getStart(), page.getCount());
        List<Order> orders = orderService.list();
        page.setTotal((int) new PageInfo<>(orders).getTotal());
        model.addAttribute("page", page);
        for (Order order : orders) {
            orderItemService.fill(order);
        }
        model.addAttribute("os", orders);
        return "admin/listOrder";
    }

    @RequestMapping("admin_order_delivery")
    public String delivery(Order order) {
        order.setDeliveryDate(new Date());
        order.setStatus(OrderService.WAIT_CONFIRM);
        orderService.update(order);
        return "redirect:admin_order_list";
    }
}