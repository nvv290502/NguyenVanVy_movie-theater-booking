package com.movie_theaters.service;

import com.movie_theaters.dto.MovieDTO;
import com.movie_theaters.dto.request.MovieRequest;
import com.movie_theaters.dto.response.MovieRating;
import com.movie_theaters.dto.response.MovieSales;
import com.movie_theaters.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface MovieService {
    MovieDTO saveMovie(MovieDTO movieDTO);

    Page<MovieDTO> getAllMovie(int page, int size, String column, String sortType);

    Page<MovieDTO> getAllMovieIsEnable(int page, int size, String column, String sortType, LocalDate date, Integer startDuration, Integer endDuration,
                                       String language, Long categoryId);

    String updateMovie(MovieRequest movieRequest, MultipartFile imageSmall, MultipartFile imageLarge);

    MovieDTO finMovieById(Long id);

    String updateIsEnable(Long id, Boolean isEnable);

    MovieDTO searchMovieByName(String name);

    List<MovieDTO> getMovieShowingByDate(LocalDate date);

    List<MovieDTO> getMovieUpcomingShow(LocalDate startDate, LocalDate endDate);

    List<Movie> getMovieByCategoryId(List<Long> categoryIds);

    List<Movie> getMovieByShowTime(LocalTime showTime, LocalDate showDate, Long cinemaId);

    Map<Long, Double> getTop10Movie();

    List<MovieDTO> getMovieIsShowing();
}
