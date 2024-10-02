package com.movie_theaters.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie_theaters.dto.SeatDTO;
import com.movie_theaters.entity.Seat;
import com.movie_theaters.service.SeatService;

import lombok.RequiredArgsConstructor;

/*
 * Author:tungnt
 */
@RestController
@RequestMapping("/api/pub")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SeatController {
    @Autowired
    private final SeatService seatService;

    @PostMapping("/seat")
    public ResponseEntity<?> createSeat(@RequestBody Seat seat) {
        try {
            Seat newSeat = seatService.saveSeat(seat);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(newSeat);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
        }
    }
}
