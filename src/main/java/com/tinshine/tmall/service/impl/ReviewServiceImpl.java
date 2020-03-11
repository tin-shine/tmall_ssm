package com.tinshine.tmall.service.impl;

import com.tinshine.tmall.mapper.ReviewMapper;
import com.tinshine.tmall.pojo.Review;
import com.tinshine.tmall.pojo.ReviewExample;
import com.tinshine.tmall.service.ReviewService;
import com.tinshine.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewMapper reviewMapper;

    @Autowired
    UserService userService;

    @Override
    public void add(Review review) {
        reviewMapper.insert(review);
    }

    @Override
    public void delete(int id) {
        reviewMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Review get(int id) {
        return reviewMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Review> list(int pid) {
        ReviewExample example = new ReviewExample();
        example.createCriteria().andPidEqualTo(pid);
        example.setOrderByClause("id desc");
        List<Review> reviews = reviewMapper.selectByExample(example);
        for (Review review : reviews) {
            review.setUser(userService.get(review.getUid()));
        }
        return reviews;
    }

    @Override
    public int getCount(int pid) {
        List<Review> reviews = list(pid);
        return reviews == null ? 0 : reviews.size();
    }
}
