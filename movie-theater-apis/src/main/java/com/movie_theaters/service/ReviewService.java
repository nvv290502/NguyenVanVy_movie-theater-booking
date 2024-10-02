package com.movie_theaters.service;

import com.movie_theaters.dto.request.ReviewRequest;
import com.movie_theaters.dto.response.MovieReviewResponse;
import com.movie_theaters.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    MovieReviewResponse getMovieReviewInfo(Long movieId);
    Review saveReview(ReviewRequest request);
    Page<Review> getReviewByMovieOrByUser(int page, int size, Long movieId, Long userId);

}
