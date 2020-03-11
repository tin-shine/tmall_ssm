package com.tinshine.tmall.controller;

import com.tinshine.tmall.pojo.*;
import com.tinshine.tmall.service.*;
import com.tinshine.tmall.util.ProductComparator.*;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("")
public class ForeController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    ProductImageService productImageService;

    @Autowired
    PropertyValueService propertyValueService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    OrderService orderService;

    @RequestMapping("forehome")
    public String home(Model model) {
        List<Category> categoryList = categoryService.list();
        for (Category category : categoryList) {
            productService.fill(category);
        }
        productService.fillByRow(categoryList);
        model.addAttribute("cs", categoryList);
        return "fore/home";
    }

    @RequestMapping("foreregister")
    public String register(Model model, User user) {
        String name = HtmlUtils.htmlEscape(user.getName());
        user.setName(name);
        if (userService.isExist(user.getName())) {
            model.addAttribute("msg", "用户名已存在");
            model.addAttribute("user", null);
            return "fore/register";
        }
        userService.add(user);
        return "redirect:registerSuccessPage";
    }

    @RequestMapping("forelogin")
    public String login(HttpSession session, Model model, @RequestParam("name") String name, @RequestParam("password") String password) {
        name = HtmlUtils.htmlEscape(name);
        User user = userService.get(name, password);
        if (user == null) {
            model.addAttribute("msg", "未找到用户信息");
            return "fore/login";
    }
        session.setAttribute("user", user);
        return "redirect:forehome";
    }

    @RequestMapping("forelogout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:forehome";
    }

    @RequestMapping("foreproduct")
    public String product(int pid, Model model) {
        Product product = productService.get(pid);
        List<ProductImage> productImages = productImageService.list(product.getId(), ProductImageService.SIMPLE);
        product.setProductImages(productImages);
        List<ProductImage> productDetailImages = productImageService.list(product.getId(), ProductImageService.DETAIL);
        product.setProductDetailImages(productDetailImages);
        model.addAttribute("p", product);
        List<PropertyValue> propertyValues = propertyValueService.list(product.getId());
        model.addAttribute("pvs", propertyValues);
        List<Review> reviews = reviewService.list(product.getId());
        model.addAttribute("reviews", reviews);
        return "fore/product";
    }

    @RequestMapping("forecheckLogin")
    @ResponseBody
    public String checkLogin(HttpSession session) {
        return session.getAttribute("user") == null ? "fail" : "success";
    }

    @RequestMapping("foreloginAjax")
    @ResponseBody
    public String modalLogin(@Param("name") String name, @Param("password") String password, HttpSession session) {
        User user = userService.get(name, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "success";
        }
        return "fail";
    }

    @RequestMapping("forecategory")
    public String category(int cid, String sort, Model model) {
        Category category = categoryService.get(cid);
        productService.fill(category);
        List<Product> products = category.getProducts();
        for (Product product : products) {
            productService.setSaleAndReviewNumber(product);
        }
        if (sort != null) {
            switch (sort) {
                case "review":
                    Collections.sort(category.getProducts(), new ReviewComparator());
                    break;
                case "date":
                    Collections.sort(category.getProducts(), new DateComparator());
                    break;
                case "saleCount":
                    Collections.sort(category.getProducts(), new SaleComparator());
                    break;
                case "price":
                    Collections.sort(category.getProducts(), new PriceComparator());
                    break;
                case "all":
                    Collections.sort(category.getProducts(), new GeneralComparator());
                    break;
            }
        }
        model.addAttribute("c", category);
        return "fore/category";
    }

    @RequestMapping("foresearch")
    public String search(String keyword, Model model) {
        List<Product> products = productService.search(keyword);
        model.addAttribute("ps", products);
        return "fore/searchResult";
    }

    @RequestMapping("forebuyone")
    public String buyone(int pid, int num, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Product product = productService.get(pid);
        product.setStock(product.getStock() - num);
        productService.update(product);
        List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
        int oiid = 0;
        boolean found = false;
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getProduct().getId() == pid) {
                orderItem.setNumber(orderItem.getNumber() + num);
                orderItemService.update(orderItem);
                found = true;
                oiid = orderItem.getId();
                break;
            }
        }
        if (!found) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(productService.get(pid));
            orderItem.setNumber(num);
            orderItem.setPid(pid);
            orderItemService.add(orderItem);
            oiid = orderItem.getId();
        }
        return "redirect:forebuy" + "?oiid=" + oiid;
    }

    @RequestMapping("forebuy")
    public String buy(Model model, String[] oiid, HttpSession session) {
        List<OrderItem> orderItems = new ArrayList<>();
        float total = 0;
        for (String id : oiid) {
            OrderItem orderItem = orderItemService.get(Integer.parseInt(id));
            orderItem.setProduct(productService.get(orderItem.getPid()));
            total += orderItem.getNumber() * orderItem.getProduct().getPromotePrice();
            orderItems.add(orderItem);
        }
        session.setAttribute("ois", orderItems);
        model.addAttribute("total", total);
        return "fore/buy";
    }

    @RequestMapping("foreaddCart")
    @ResponseBody
    public String addCart(int pid, int num, HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
        boolean found = false;
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getProduct().getId() == pid) {
                found = true;
                orderItem.setNumber(orderItem.getNumber() + num);
                orderItemService.update(orderItem);
                break;
            }
        }
        if (!found) {
            OrderItem orderItem = new OrderItem();
            orderItem.setNumber(num);
            orderItem.setUid(user.getId());
            orderItem.setPid(pid);
            orderItemService.add(orderItem);
        }
        return "success";
    }

    @RequestMapping("forecart")
    public String cart(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
        model.addAttribute("ois", orderItems);
        return "fore/cart";
    }

    @RequestMapping("forechangeOrderItem")
    @ResponseBody
    public String changeOrderItem(int pid, int number, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "fail";
        List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getProduct().getId() == pid) {
                orderItem.setNumber(number);
                orderItemService.update(orderItem);
                break;
            }
        }
        return "success";
    }

    @RequestMapping("foredeleteOrderItem")
    @ResponseBody
    public String deleteOrderItem(int oiid, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "fail";
        }
        orderItemService.delete(oiid);
        return "success";
    }

    @RequestMapping("forecreateOrder")
    public String createOrder(HttpSession session, Order order) {
        User user = (User) session.getAttribute("user");
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(1000);
        order.setOrderCode(orderCode);
        order.setUid(user.getId());
        order.setStatus(OrderService.WAIT_PAY);
        List<OrderItem> orderItems = (List<OrderItem>) session.getAttribute("ois");
        float total = orderService.add(order, orderItems);
        return "redirect:forealipay" + "?oid=" + order.getId() + "&total=" + total;
    }

    @RequestMapping("forepayed")
    public String payed(int oid, Model model) {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.WAIT_DELIVERY);
        order.setPayDate(new Date());
        orderService.update(order);
        model.addAttribute("o", order);
        return "fore/payed";
    }

    @RequestMapping("forebought")
    public String bought(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<Order> orders = orderService.list(user.getId(), OrderService.DELETE);
        for (Order order : orders) {
            orderItemService.fill(order);
        }
        model.addAttribute("os", orders);
        return "fore/bought";
    }

    @RequestMapping("foreconfirmPay")
    public String confirmPay(int oid, Model model) {
        Order order = orderService.get(oid);
        orderItemService.fill(order);
        model.addAttribute("o", order);
        return "fore/confirmPay";
    }

    @RequestMapping("foreorderConfirmed")
    public String orderConfirmed(int oid) {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.WAIT_REVIEW);
        order.setConfirmDate(new Date());
        orderService.update(order);
        return "fore/orderConfirmed";
    }

    @RequestMapping("foredeleteOrder")
    @ResponseBody
    public String deleteOrder(int oid) {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.DELETE);
        orderService.update(order);
        return "success";
    }

    @RequestMapping("forereview")
    public String review(int oid, Model model) {
        Order order = orderService.get(oid);
        orderItemService.fill(order);
        model.addAttribute("o", order);
        Product product = order.getOrderItems().get(0).getProduct();
        productService.setSaleAndReviewNumber(product);
        model.addAttribute("p", product);
        List<Review> reviews = reviewService.list(product.getId());
        model.addAttribute("reviews", reviews);
        return "fore/review";
    }

    @RequestMapping("foredoreview")
    public String doReview(Model model, HttpSession session, @Param("pid") int pid, @Param("oid") int oid, String content) {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.FINISHED);
        orderService.update(order);

        Review review = new Review();
        content = HtmlUtils.htmlEscape(content);
        review.setContent(content);
        review.setCreateDate(new Date());
        review.setPid(pid);
        User user = (User) session.getAttribute("user");
        review.setUid(user.getId());
        reviewService.add(review);

        model.addAttribute("oid", oid);
        return "redirect:forereview" + "?oid=" + oid + "&showonly=true";
    }
}