package com.movie_theater_frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegister() {
        return "register";
    }

    @GetMapping("/home")
    public String getHome() {
        return "client/index";
    }

    @GetMapping("/header")
    public String getHeader() {
        return "client/header";
    }

    @GetMapping("/footer")
    public String getFooter() {
        return "client/footer";
    }

    @GetMapping("/forgot-password")
    public String getForgotPassword() {
        return "forgot-password";
    }

    @GetMapping("/user-info")
    public String getUserInfo() {
        return "client/user-info";
    }

    @GetMapping("/user/personal-information")
    public String getPersonalInfo() {
        return "client/personal-infor";
    }

    @GetMapping("/user-info/history-movie")
    public String getHistoryMovie() {
        return "client/history-watch-movie";
    }

    @GetMapping("/user-info/membership-level")
    public String getMembersipLevelInfo() {
        return "client/membership-level";
    }

    @GetMapping("/admin/customer-detail")
    public String getCustomerDetail() {
        return "admin/customer-info-detail";
    }
}
