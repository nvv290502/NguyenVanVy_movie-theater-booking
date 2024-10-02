package com.movie_theaters.service.impl;

import com.movie_theaters.dto.request.ReviewRequest;
import com.movie_theaters.dto.response.MovieReviewResponse;
import com.movie_theaters.entity.Movie;
import com.movie_theaters.entity.Review;
import com.movie_theaters.entity.User;
import com.movie_theaters.exception.EmptyListException;
import com.movie_theaters.exception.ObjectNotFoundException;
import com.movie_theaters.repository.MovieRepository;
import com.movie_theaters.repository.ReviewRepository;
import com.movie_theaters.repository.UserRepository;
import com.movie_theaters.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    double averageRating = 0;
    int commentCount = 0;
    int countRating = 0;
    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    @Override
    public MovieReviewResponse getMovieReviewInfo(Long movieId) {
        List<Review> reviews = reviewRepository.getByMovie(movieId);
        if(reviews.isEmpty()){
            throw new EmptyListException("Phim chưa có đánh giá nào!");
        }

        List<Integer> ratings = new ArrayList<>();
        List<String> comments = new ArrayList<>();

        for (Review review : reviews) {
            if (review.getNumberStar() != null) {
                ratings.add(review.getNumberStar());
            }
            if (review.getComment() != null && !review.getComment().isEmpty()) {
                comments.add(review.getComment());
            }
        }

        averageRating = ratings.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        DecimalFormat df = new DecimalFormat("#.0");
        String formattedAverageRating = df.format(averageRating);

        commentCount = comments.size();
        countRating = ratings.size();

        Map<Integer, Long> ratingCounts = reviews.stream()
                .collect(Collectors.groupingBy(Review::getNumberStar, Collectors.counting()));

        MovieReviewResponse movieReviewResponse = new MovieReviewResponse();
        movieReviewResponse.setMovieId(movieId);
        movieReviewResponse.setRating(Double.parseDouble(formattedAverageRating));
        movieReviewResponse.setCountComment(commentCount);
        movieReviewResponse.setCountRating(countRating);
        movieReviewResponse.setRatingCounts(ratingCounts);
        return movieReviewResponse;
    }

    @Override
    public Review saveReview(ReviewRequest request) {
        Optional<Movie> movie = movieRepository.findById(request.getMovieId());
        if(movie.isEmpty()){
            throw new ObjectNotFoundException("Phim không tồn tại!");
        }
        Optional<User> user = userRepository.findById(request.getUserId());
        if(user.isEmpty()){
            throw new ObjectNotFoundException("Người dùng không tồn tại!");
        }
        Review review = new Review();
        review.setComment(request.getComment());
        review.setNumberStar(request.getNumberStar());
        review.setMovie(movie.get());
        review.setUser(user.get());
        return reviewRepository.save(review);
    }

    @Override
    public Page<Review> getReviewByMovieOrByUser(int page,int size, Long movieId, Long userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdDate")));
        return reviewRepository.getReviewByMovieOrByUser(pageable, movieId, userId);
    }
}
