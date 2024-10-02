package com.movie_theater_frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShopController {

    @GetMapping("/shop")
    public String getShop(){
        return "client/shop";
    }

    @GetMapping("/shop-food")
    public String getShopFood(){
        return "client/shop-food";
    }

    @GetMapping("/bill-food")
    public String getBillFood(){
        return "client/form-payment-food";
    }

}
