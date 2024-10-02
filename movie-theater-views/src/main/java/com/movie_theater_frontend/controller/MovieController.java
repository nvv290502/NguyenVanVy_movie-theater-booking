package com.movie_theater_frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movie")
public class MovieController {

    @GetMapping
    public String getMovieDetail() {
        return "client/movie-details";
    }

    @GetMapping("/info")
    public String getDetailInfo() {
        return "client/thongtin";
    }

    @GetMapping("/showtime")
    public String getDetailShowtime() {
        return "client/lichchieu";
    }

    @GetMapping("/review")
    public String getDetailReview() {
        return "client/danhgia";
    }

    @GetMapping("/related-movie")
    public String getRelatedMovie() {
        return "client/related-movie";
    }

    @GetMapping("/buy-ticket")
    public String getBuyTicket() {
        return "client/buy-ticket";
    }

    @GetMapping("/list")
    public String getAll() {
        return "client/all-movie";
    }

    @GetMapping("/manager")
    public String getMovieManager() {
        return "admin/movies-manager";
    }

    @GetMapping("/category-manager")
    public String getCategoryManager() {
        return "admin/categories-manager";
    }

    @GetMapping("/showing-today")
    public String getMovieShowingToDay() {
        return "client/movie-showing-today";
    }

    @GetMapping("/movie-upcoming-show")
    public String getMovieUpcomingShow() {
        return "client/movie-upcoming-show";
    }

    @GetMapping("/movie-top-score")
    public String getMovieTopScore() {
        return "client/movie-top-score";
    }

}
