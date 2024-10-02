package com.movie_theaters.repository;

import com.movie_theaters.dto.response.MovieRating;
import com.movie_theaters.dto.response.MovieSales;
import com.movie_theaters.util.MovieRatingExtractor;
import com.movie_theaters.util.MovieSalesExtractor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MovieRepositoryCustomImpl implements MovieRepositoryCustom {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MovieRepositoryCustomImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<MovieSales> getTop10MovieByNumberTicket() {
        String sql = "SELECT m.movie_id AS movieId, COUNT(bd.seat_id) AS numberTicket " +
                "FROM bill_detail bd " +
                "RIGHT JOIN schedules sch ON sch.schedule_id = bd.schedule_id " +
                "RIGHT JOIN movies m ON m.movie_id = sch.movie_id " +
                "GROUP BY m.movie_id " +
                "ORDER BY NumberTicket DESC";

        return jdbcTemplate.query(sql, new MovieSalesExtractor());
    }

    @Override
    public List<MovieRating> getTop10MovieByRating() {
        String sql = "SELECT m.movie_id AS movieId, ROUND(AVG(CAST(r.number_star AS FLOAT)), 2) AS rating FROM review r " +
                "RIGHT JOIN movies m ON m.movie_id = r.movie_id " +
                "GROUP BY m.movie_id " +
                "ORDER BY Rating DESC;";

        return jdbcTemplate.query(sql, new MovieRatingExtractor());
    }
}
