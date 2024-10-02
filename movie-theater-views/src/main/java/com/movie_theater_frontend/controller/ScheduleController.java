package com.movie_theater_frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ScheduleController {

    @GetMapping("/schedule-manager")
    public String getScheduleManager() {
        return "admin/schedule-manager";
    }

    @GetMapping("/test")
    public String getTest() {
        return "admin/test";
    }

    @GetMapping("/schedule-movie")
    public String getScheduleMovie() {
        return "admin/schedule-movie";
    }

    @GetMapping("/set-showtime")
    public String getSetShowtime() {
        return "admin/set-showtime";
    }

    @GetMapping("/showtime-manager")
    public String getShowtimeManager() {
        return "admin/showtime-manager";
    }
}
