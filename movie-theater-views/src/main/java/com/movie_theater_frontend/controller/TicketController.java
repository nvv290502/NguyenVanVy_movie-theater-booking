package com.movie_theater_frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/booking")
public class TicketController {

    @GetMapping
    public String getProcessBooking(){
        return "client/ticket-booking-process";
    }

    @GetMapping("/progress")
    public String getProgress(){
        return "client/progress";
    }

    @GetMapping("/select-room")
    public String getSelectRoom(){
        return "client/select-room";
    }

    @GetMapping("/select-seat")
    public String getSelectSeat(){
        return "client/select-seats";
    }

    @GetMapping("/food")
    public String getBookingFood(){
        return "client/booking-more";
    }
}
