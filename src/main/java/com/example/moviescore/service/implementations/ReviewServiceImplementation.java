package com.example.moviescore.service.implementations;

import com.example.moviescore.models.Review;
import com.example.moviescore.repositories.ReviewRepository;
import com.example.moviescore.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImplementation implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImplementation(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> getAllReviews() {
        List<Review> reviewList = reviewRepository.findAll();


        return reviewList;
    }
}
