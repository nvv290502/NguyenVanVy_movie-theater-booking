package com.movie_theaters.repository;

import com.movie_theaters.dto.MovieDTO;
import com.movie_theaters.dto.response.MovieSales;
import com.movie_theaters.entity.Movie;
import com.movie_theaters.entity.enums.StatusMovie;

import com.movie_theaters.util.NativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, MovieRepositoryCustom {

        Page<Movie> findAll(Pageable pageable);

        @Query("SELECT DISTINCT m FROM Movie m " +
                "LEFT JOIN m.schedules sch " +
                "JOIN m.categories cat " +
                "WHERE (:date IS NULL OR sch.date = :date) " +
                "AND (:start IS NULL OR :end IS NULL OR m.duration BETWEEN :start AND :end) " +
                "AND (:language IS NULL OR m.language LIKE CONCAT('%', :language, '%')) "+
                "AND (:categoryId IS NULL OR cat.id = :categoryId) " +
                "AND m.isEnable = :isEnable")
        Page<Movie> getMovieByFilters(
                Pageable pageable,
                @Param("date") LocalDate date,
                @Param("start") Integer start,
                @Param("end") Integer end,
                @Param("language") String language,
                @Param("categoryId") Long categoryId,
                @Param("isEnable") Boolean isEnable
        );

        Boolean existsByNameAndSummary(String name, String summary);

        Movie getByName(String name);

        @Query("SELECT m FROM Schedule sch " +
                        "JOIN sch.movie m " +
                        "JOIN sch.showtimes s " +
                        "WHERE sch.date = :date " +
                        "AND m.isEnable = true")
        List<Movie> getMovieShowingByDate(LocalDate date);

        List<Movie> findByStatusOrderByReleasedDateDesc(StatusMovie status);

        @Query("SELECT m FROM Movie m " +
                        "WHERE m.releasedDate BETWEEN :startDate AND :endDate " +
                        "AND m.isEnable = true")
        List<Movie> getMovieUpcomingShow(LocalDate startDate, LocalDate endDate);

        @Query("SELECT m FROM Movie m " +
                        "JOIN m.categories c " +
                        "WHERE c.id IN :categoryIds")
        List<Movie> getMovieByListCategoryId(List<Long> categoryIds);

        @Query("SELECT m FROM Room r " +
                        "JOIN r.showtimes s " +
                        "JOIN s.schedule sch " +
                        "JOIN sch.movie m " +
                        "WHERE r.cinema.id = :cinemaId AND " +
                        "(sch.time = :showTime) AND " +
                        "(sch.date = :showDate) AND " +
                        "(m.isEnable = true)")
        List<Movie> getMovieByShowTime(LocalTime showTime, LocalDate showDate, Long cinemaId);

        // @Query(value = "SELECT TOP(10) new
        // com.movie_theaters.response.MovieTopDTO(m.movie_id, COUNT(bd.seat_id) AS
        // NumberTicket) FROM bill_detail bd " +
        // "INNER JOIN schedules sch ON sch.schedule_id = bd.schedule_id " +
        // "INNER JOIN movies m ON m.movie_id = sch.movie_id " +
        // "GROUP BY m.movie_id " +
        // "ORDER BY NumberTicket DESC",
        // nativeQuery = true)
        // List<MovieSales> getTop10MovieByNumberTicket();
        List<Movie> findByStatus(StatusMovie status);

        @Query(value = NativeQuery.MOVIE_REVENUE_QUERY, nativeQuery = true)
        List<Object[]> getMovieRevenue(LocalDateTime startDate, LocalDateTime endDate);
        @Query("SELECT m.id, m.name FROM Movie m")
        List<Object[]> getAllMovies();
}
