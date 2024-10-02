package com.movie_theaters.util;

import com.movie_theaters.dto.response.MovieSales;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieSalesExtractor implements RowMapper<MovieSales> {
    @Override
    public MovieSales mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MovieSales(
                rs.getLong("movieId"),
                rs.getInt("numberTicket")
        );
    }
}
