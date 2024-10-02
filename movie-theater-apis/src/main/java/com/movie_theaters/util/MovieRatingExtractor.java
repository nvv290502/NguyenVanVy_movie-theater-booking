package com.movie_theaters.util;

import com.movie_theaters.dto.response.MovieRating;
import com.movie_theaters.dto.response.MovieSales;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieRatingExtractor implements RowMapper<MovieRating> {
    @Override
    public MovieRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MovieRating(
                rs.getLong("movieId"),
                rs.getFloat("rating")
        );
    }
}
