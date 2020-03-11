package com.tinshine.tmall.util.ProductComparator;

import com.tinshine.tmall.pojo.Product;

import java.util.Comparator;

public class PriceComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return (int) (p2.getPromotePrice() - p1.getPromotePrice());
    }
}
