package com.tinshine.tmall.util.ProductComparator;

import com.tinshine.tmall.pojo.Product;

import java.util.Comparator;

public class ReviewComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return p2.getReviewCount() - p1.getReviewCount();
    }
}
