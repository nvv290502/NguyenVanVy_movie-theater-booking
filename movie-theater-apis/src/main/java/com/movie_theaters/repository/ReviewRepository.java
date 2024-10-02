package com.movie_theaters.repository;

import com.movie_theaters.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.movie.id = :movieId ")
    List<Review> getByMovie(Long movieId);

    @Query("SELECT r FROM Review r "+
            "WHERE (r.movie.id = :movieId OR :movieId IS NULL) "+
            "AND (r.user.id = :userId OR :userId IS NULL)")
    Page<Review> getReviewByMovieOrByUser(Pageable pageable, Long movieId, Long userId);
}
