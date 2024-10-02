package com.movie_theaters.service;

import com.movie_theaters.dto.RoomSeatDTO;
import com.movie_theaters.dto.request.SeatLayoutRequest;
import com.movie_theaters.entity.RoomSeat;

import java.util.List;

public interface RoomSeatService {
    List<RoomSeatDTO> getSeatByRoom(Long roomId);
    List<RoomSeatDTO> saveLayout(Long roomId, List<SeatLayoutRequest> requests);
    List<RoomSeat> updateStatusSeat(Long roomId, List<SeatLayoutRequest> requests);
}
