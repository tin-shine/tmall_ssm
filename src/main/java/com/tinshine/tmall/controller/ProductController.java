package com.tinshine.tmall.controller;

import com.github.pagehelper.PageInfo;
import com.tinshine.tmall.pojo.Category;
import com.tinshine.tmall.pojo.Product;
import com.tinshine.tmall.service.CategoryService;
import com.tinshine.tmall.service.ProductService;
import com.tinshine.tmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("")
public class ProductController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @RequestMapping("admin_product_add")
    public String add(Product product) {
        product.setCreateDate(new Date());
        productService.add(product);
        return "redirect:admin_product_list?cid=" + product.getCid();
    }

    @RequestMapping("admin_product_delete")
    public String delete(int id) {
        Product product = productService.get(id);
        productService.delete(id);
        return "redirect:admin_product_list?cid=" + product.getCid();
    }

    @RequestMapping("admin_product_edit")
    public String edit(int id, Model model) {
        Product product = productService.get(id);
        product.setCategory(categoryService.get(product.getCid()));
        model.addAttribute("product", product);
        return "admin/editProduct";
    }

    @RequestMapping("admin_product_update")
    public String update(Product product) {
        productService.update(product);
        return "redirect:admin_product_list?cid=" + product.getCid();
    }

    @RequestMapping("admin_product_list")
    public String list(int cid, Model model, Page page) {
        Category category = categoryService.get(cid);
        model.addAttribute("category", category);
        List<Product> products = productService.list(cid);
        model.addAttribute("products",products);
        page.setTotal((int) new PageInfo<>().getTotal());
        page.setParam("&cid=" + category.getId());
        model.addAttribute("page", page);
        return "admin/listProduct";
    }
}
