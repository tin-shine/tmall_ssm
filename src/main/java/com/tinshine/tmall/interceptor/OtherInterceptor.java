package com.tinshine.tmall.interceptor;

import com.tinshine.tmall.controller.CategoryController;
import com.tinshine.tmall.pojo.Category;
import com.tinshine.tmall.pojo.OrderItem;
import com.tinshine.tmall.pojo.User;
import com.tinshine.tmall.service.CategoryService;
import com.tinshine.tmall.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class OtherInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    CategoryService categoryService;

    @Autowired
    OrderItemService orderItemService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        /*这里是获取分类集合信息，用于放在搜索栏下面*/
        List<Category> cs = categoryService.list();
        request.getSession().setAttribute("cs", cs);

        /*这里是获取当前的contextPath:tmall_ssm,用于放在左上角那个变形金刚，点击之后才能够跳转到首页，否则点击之后也仅仅停留在当前页面*/
        HttpSession session = request.getSession();
        String contextPath = session.getServletContext().getContextPath();
        request.getSession().setAttribute("contextPath", contextPath);

        /*这里是获取购物车中一共有多少数量*/
        User user =(User) session.getAttribute("user");
        int cartTotalItemNumber = 0;
        if(null != user) {
            List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
            for (OrderItem orderItem : orderItems) {
                cartTotalItemNumber += orderItem.getNumber();
            }

        }
        request.getSession().setAttribute("cartTotalItemNumber", cartTotalItemNumber);
    }
}
