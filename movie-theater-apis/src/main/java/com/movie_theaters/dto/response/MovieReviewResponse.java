package com.movie_theaters.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieReviewResponse {
    private Long movieId;
    private Integer countComment;
    private Integer countRating;
    private double rating;
    private Map<Integer, Long> ratingCounts;
}
