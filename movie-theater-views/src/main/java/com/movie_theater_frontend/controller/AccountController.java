package com.movie_theater_frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    @GetMapping("/account-manager")
    public String getAccountManager(){
        return "admin/account-manager";
    }
    
    @GetMapping("/staff-manager")
    public String getStaffManager(){
        return "admin/staff-manager";
    }
}
