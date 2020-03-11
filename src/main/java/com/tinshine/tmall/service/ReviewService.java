package com.tinshine.tmall.service;

import com.tinshine.tmall.pojo.Review;

import java.util.List;

public interface ReviewService {
    void add(Review review);
    void delete(int id);
    Review get(int id);
    List<Review> list(int pid);
    int getCount(int pid);
}
