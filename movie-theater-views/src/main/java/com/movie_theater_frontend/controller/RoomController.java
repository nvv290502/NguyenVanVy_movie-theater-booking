package com.movie_theater_frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoomController {

    @GetMapping("/room-manager")
    public String getRoomManager() {
        return "admin/room-manager";
    }

    @GetMapping("/rooms-manager")
    public String getRoomsManager() {
        return "admin/rooms-manager";
    }

    @GetMapping("/room-details")
    public String getRoomDetails() {
        return "admin/room-details";
    }

    @GetMapping("/room-layout")
    public String getRoomLayout() {
        return "admin/seat-manager";
    }

}
