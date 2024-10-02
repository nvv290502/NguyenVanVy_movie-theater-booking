package com.movie_theater_frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CinemaController {

    @GetMapping("/cinema")
    public String getCinema() {
        return "client/cinema";
    }

    @GetMapping("/cinema/detail")
    public String getCinemaDetail() {
        return "client/cinema-detail";
    }

    @GetMapping("/cinema-manager")
    public String getCinemaManager() {
        return "admin/cinema-manager";
    }

    @GetMapping("/cinemas-manager")
    public String getCinemasManager() {
        return "admin/cinemas-manager";
    }

}
