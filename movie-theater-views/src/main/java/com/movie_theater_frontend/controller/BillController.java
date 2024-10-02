package com.movie_theater_frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BillController {

    @GetMapping("/count-down")
    public String getCountDown() {
        return "client/count-down";
    }

    @GetMapping("/bill-manager")
    public String getBillManagement() {
        return "admin/bill-manager";
    }

    @GetMapping("/bill-detail")
    public String getBillDetail() {
        return "admin/bill-manager-detail";
    }
}