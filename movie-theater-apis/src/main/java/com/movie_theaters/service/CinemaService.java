package com.movie_theaters.service;

import com.movie_theaters.dto.CinemaDTO;
import com.movie_theaters.dto.MovieDTO;
import com.movie_theaters.dto.request.CinemaRequest;
import com.movie_theaters.entity.Cinema;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

/*
 * Author:tungnt
 */

public interface CinemaService {
    List<Cinema> findAllCinema();
    Cinema findCinemaById(Long cinemaId);
    Cinema saveCinema(CinemaRequest request, MultipartFile imageUrl);
    Cinema updateCinema(Long cinemaId, CinemaRequest request, MultipartFile imageUrl);
    List<Cinema> getCinemaByMovieShowTime(Long movieId, String city, LocalDate showDate);
    Cinema deactiveCinema(Long cinemaId);
    Cinema activeCinema(Long cinemaId);
    List<Cinema> findAllActiveCinema();
    String updateIsEnable(Long id, Boolean isEnable);
    CinemaDTO searchCinemaByName(String name);
}
