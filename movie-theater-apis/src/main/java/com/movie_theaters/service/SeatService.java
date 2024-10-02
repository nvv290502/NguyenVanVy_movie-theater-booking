package com.movie_theaters.service;

import com.movie_theaters.dto.request.SeatLayoutRequest;
import com.movie_theaters.entity.Seat;

import java.util.List;

public interface SeatService {
    Seat saveSeat(Seat seat);
    List<Seat> saveAllSeat(List<SeatLayoutRequest> request);
}
