package com.movie_theaters.repository;

import com.movie_theaters.entity.Category;
import com.movie_theaters.entity.Cinema;

import com.movie_theaters.entity.Movie;
import com.movie_theaters.util.NativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/*
 * Author:tungnt
 */

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    List<Cinema> findAllByIsEnabledTrue();

    @Query("SELECT DISTINCT c FROM Cinema c " +
            "JOIN c.rooms r " +
            "JOIN r.showtimes s " +
            "JOIN s.schedule sch " +
            "WHERE sch.movie.id = :movieId AND " +
            "(c.address LIKE %:city% OR :city IS NULL) AND " +
            "(sch.date = :showDate OR :showDate IS NULL) AND "+
            "sch.date >= :currentDate")
    List<Cinema> getCinemaByMovieShowTime(Long movieId, String city, LocalDate showDate, LocalDate currentDate);

    Page<Cinema> getByIsEnabled(Pageable pageable, Boolean isEnable);
    Page<Cinema> findAll(Pageable pageable);
    Cinema getByName(String name);
    @Query(value = NativeQuery.CINEMA_REVENUE_QUERY, nativeQuery = true)
    List<Object[]> getCinemaRevenue(LocalDateTime startDate, LocalDateTime endDate);
    @Query("SELECT c.id, c.name FROM Cinema c")
    List<Object[]> getAllCinema();
}
