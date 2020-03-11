package com.tinshine.tmall.util.ProductComparator;

import com.tinshine.tmall.pojo.Product;

import java.util.Comparator;

public class GeneralComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return p2.getReviewCount() * p2.getSaleCount() - p1.getReviewCount() * p1.getSaleCount();
    }
}
