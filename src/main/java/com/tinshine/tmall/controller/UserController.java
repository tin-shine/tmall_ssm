package com.tinshine.tmall.controller;

import com.tinshine.tmall.pojo.User;
import com.tinshine.tmall.service.impl.UserServiceImpl;
import com.tinshine.tmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @RequestMapping("admin_user_list")
    public String list(Model model, Page page) {
        List<User> userList = userService.list();
        model.addAttribute("userList", userList);
        return "admin/listUser";
    }

}
