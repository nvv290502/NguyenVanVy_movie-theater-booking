package com.movie_theaters.service;

import com.movie_theaters.dto.CinemaDTO;
import com.movie_theaters.dto.RoomDTO;
import com.movie_theaters.dto.common.RoomSeatCreateDTO;
import com.movie_theaters.dto.SeatDTO;
import com.movie_theaters.dto.common.RoomEarlyInitialDTO;
import com.movie_theaters.dto.common.RoomHasLayoutDTO;
import com.movie_theaters.dto.common.RoomHasRoomSeatDTO;
import com.movie_theaters.dto.request.RoomRequest;
import com.movie_theaters.entity.Room;
import com.movie_theaters.entity.enums.TypeSeat;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/*
 * Author:tungnt
 */

public interface RoomService {
    List<RoomEarlyInitialDTO> findAllRoomByCinemaId(Long cinemaId);

    Room findRoomById(Long roomId);

    Room saveRoom(RoomEarlyInitialDTO roomEarlyInitialDTO, Long cinemaId);

    Room updateRoom(RoomEarlyInitialDTO roomEarlyInitialDTO, Long cinemaId, Long roomId);

    List<RoomDTO> getRoomShowTime(Long movieId, LocalTime showTime, LocalDate showDate, Long cinemaId);

    Room updateRoom(RoomHasLayoutDTO roomHasLayoutDTO, Long cinemaId, Long roomId);

    Room saveRoom(Room room);

    Room saveRoom(RoomHasRoomSeatDTO roomHasRoomSeatDTO, Long roomId);

    RoomHasLayoutDTO findRoomLayoutById(Long roomId);

    RoomSeatCreateDTO findRoomSeatStatus(Long roomId, SeatDTO seatDTO);

    RoomSeatCreateDTO updateDisabledSeat(Long roomId, Long seatId, TypeSeat typeSeat);

    List<Room> findAllActiveRoomByCinemaId(Long cinemaId);
    Page<Room> getRoomByCinema(Long cinemaId, int page, int size, String column, String sortType);
    String updateIsEnable(Long id, Boolean isEnable);
    RoomDTO searchRoomByName(String name, Long cinemaId);
    Room updateRoom(Long roomId, RoomRequest request);
    Room saveRoom(RoomRequest request);
    Room updateInitialization(Long roomId, int seatRowNumbers, int seatColumnNumbers);
}
