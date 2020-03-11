package com.tinshine.tmall.controller;

import com.tinshine.tmall.pojo.Product;
import com.tinshine.tmall.pojo.PropertyValue;
import com.tinshine.tmall.service.ProductService;
import com.tinshine.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("")
public class PropertyValueController {

    @Autowired
    ProductService productService;

    @Autowired
    PropertyValueService propertyValueService;

    @RequestMapping("admin_propertyValue_edit")
    public String edit(int pid, Model model) {
        Product product = productService.get(pid);
        propertyValueService.init(product);
        model.addAttribute("product", product);
        List<PropertyValue> propertyValues = propertyValueService.list(product.getId());
        model.addAttribute("pvs", propertyValues);
        return "admin/editPropertyValue";
    }

    @RequestMapping("admin_propertyValue_update")
    @ResponseBody
    public String update(PropertyValue propertyValue) {
        propertyValueService.update(propertyValue);
        return "success";
    }

}