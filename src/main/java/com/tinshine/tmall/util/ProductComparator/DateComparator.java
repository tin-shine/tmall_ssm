package com.tinshine.tmall.util.ProductComparator;

import com.tinshine.tmall.pojo.Product;

import java.util.Comparator;

public class DateComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return p2.getCreateDate().compareTo(p1.getCreateDate());
    }
}
