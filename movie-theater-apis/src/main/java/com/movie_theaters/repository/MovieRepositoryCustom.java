package com.movie_theaters.repository;

import com.movie_theaters.dto.response.MovieRating;
import com.movie_theaters.dto.response.MovieSales;

import java.util.List;

public interface MovieRepositoryCustom {
    List<MovieSales> getTop10MovieByNumberTicket();
    List<MovieRating> getTop10MovieByRating();
}
