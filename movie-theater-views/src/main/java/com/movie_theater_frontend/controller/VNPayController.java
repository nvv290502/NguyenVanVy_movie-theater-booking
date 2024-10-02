package com.movie_theater_frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VNPayController {

    @GetMapping("/vnpay-payment-return")
    public String getPagePaymentSucess(){
        return "client/vnpay-return";
    }
}
