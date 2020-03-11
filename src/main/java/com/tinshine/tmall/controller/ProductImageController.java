package com.tinshine.tmall.controller;

import com.tinshine.tmall.pojo.Product;
import com.tinshine.tmall.pojo.ProductImage;
import com.tinshine.tmall.service.CategoryService;
import com.tinshine.tmall.service.ProductImageService;
import com.tinshine.tmall.service.ProductService;
import com.tinshine.tmall.util.ImageUtil;
import com.tinshine.tmall.util.UploadedImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

@Controller
@RequestMapping("")
public class ProductImageController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductImageService productImageService;

    @RequestMapping("admin_productImage_add")
    public String add(ProductImage image, HttpSession session, UploadedImage imageFile) {
        productImageService.add(image);
        String fileName = image.getId()+ ".jpg";
        String imageFolder;
        String imageFolder_small = null;
        String imageFolder_middle = null;
        if(ProductImageService.SIMPLE.equals(image.getType())){
            imageFolder= session.getServletContext().getRealPath("img/productSingle");
            imageFolder_small= session.getServletContext().getRealPath("img/productSingle_small");
            imageFolder_middle= session.getServletContext().getRealPath("img/productSingle_middle");
        } else{
            imageFolder= session.getServletContext().getRealPath("img/productDetail");
        }

        File f = new File(imageFolder, fileName);
        f.getParentFile().mkdirs();
        try {
            imageFile.getImage().transferTo(f);
            BufferedImage img = ImageUtil.toJpg(f);
            ImageIO.write(img, "jpg", f);

            if(ProductImageService.SIMPLE.equals(image.getType())) {
                File f_small = new File(imageFolder_small, fileName);
                File f_middle = new File(imageFolder_middle, fileName);

                ImageUtil.resizeImage(f, 56, 56, f_small);
                ImageUtil.resizeImage(f, 217, 190, f_middle);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:admin_productImage_list?pid=" + image.getPid();
    }

    @RequestMapping("admin_productImage_delete")
    public String delete(int id, HttpSession session) {
        ProductImage image = productImageService.get(id);
        String fileName = image.getId()+ ".jpg";
        String imageFolder;
        String imageFolder_small = null;
        String imageFolder_middle = null;

        if(ProductImageService.SIMPLE.equals(image.getType())){
            imageFolder= session.getServletContext().getRealPath("img/productSingle");
            imageFolder_small= session.getServletContext().getRealPath("img/productSingle_small");
            imageFolder_middle= session.getServletContext().getRealPath("img/productSingle_middle");
            File imageFile = new File(imageFolder,fileName);
            File f_small = new File(imageFolder_small,fileName);
            File f_middle = new File(imageFolder_middle,fileName);
            imageFile.delete();
            f_small.delete();
            f_middle.delete();
        } else{
            imageFolder= session.getServletContext().getRealPath("img/productDetail");
            File imageFile = new File(imageFolder,fileName);
            imageFile.delete();
        }
        productImageService.delete(id);

        return "redirect:admin_productImage_list?pid=" + image.getPid();
    }

    @RequestMapping("admin_productImage_list")
    public String list(int pid, Model model) {
        Product product = productService.get(pid);
        model.addAttribute("product", product);
        List<ProductImage> imageSimple = productImageService.list(pid, ProductImageService.SIMPLE);
        model.addAttribute("pisSingle", imageSimple);
        List<ProductImage> imageDetail = productImageService.list(pid, ProductImageService.DETAIL);
        model.addAttribute("pisDetail", imageDetail);
        return "admin/listProductImage";
    }
}
