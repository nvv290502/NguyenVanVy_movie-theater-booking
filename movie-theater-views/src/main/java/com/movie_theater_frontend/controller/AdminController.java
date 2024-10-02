package com.movie_theater_frontend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String getAdmin() {
        return "admin/index";
    }

    @GetMapping("/menu")
    public String getMenu() {
        return "admin/menu";
    }

    @GetMapping("/admin/movie-revenue")
    public String movieRevenue() {
        return "admin/movie-revenue";
    }

    @GetMapping("/admin/cinema-revenue")
    public String cinemaRevenue() {
        return "admin/cinema-revenue";
    }
}
