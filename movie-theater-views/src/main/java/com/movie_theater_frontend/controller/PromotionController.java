package com.movie_theater_frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PromotionController {
    @GetMapping("/promotion-manager")
    public String getPromotionManager() {
        return "admin/promotion-manager";
    }
}
