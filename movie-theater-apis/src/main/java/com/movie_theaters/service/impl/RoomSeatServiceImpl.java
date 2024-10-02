package com.movie_theaters.service.impl;

import com.movie_theaters.dto.RoomSeatDTO;
import com.movie_theaters.dto.request.SeatLayoutRequest;
import com.movie_theaters.entity.Room;
import com.movie_theaters.entity.RoomSeat;
import com.movie_theaters.entity.Seat;
import com.movie_theaters.entity.compositekeyfields.SeatRoomId;
import com.movie_theaters.exception.EmptyListException;
import com.movie_theaters.repository.RoomRepository;
import com.movie_theaters.repository.RoomSeatRepository;
import com.movie_theaters.repository.SeatRepository;
import com.movie_theaters.service.RoomSeatService;
import com.movie_theaters.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomSeatServiceImpl implements RoomSeatService {

    private final RoomSeatRepository roomSeatRepository;
    private final RoomRepository roomRepository;
    private final SeatService seatService;
    private final SeatRepository seatRepository;
    @Override
    public List<RoomSeatDTO> getSeatByRoom(Long roomId) {
        if(roomId == null){
            throw new NullPointerException("Vui lòng nhập id phòng!");
        }
        Optional<Room> rooms = roomRepository.findById(roomId);
        if(rooms.isEmpty()){
            throw new EmptyListException("Phòng không tồn tại!");
        }
        return roomSeatRepository.getSeatByRoom(roomId);
    }
    @Override
    public List<RoomSeatDTO> saveLayout(Long roomId, List<SeatLayoutRequest> requests) {
        seatService.saveAllSeat(requests);

        List<RoomSeatDTO> roomSeatDTOS = new ArrayList<>();
        for(SeatLayoutRequest s : requests){
            Seat seat = seatRepository.findSeatsByColumnNameAndRowName(s.getColumnName(), s.getRowName());
            RoomSeatDTO roomSeatDTO = new RoomSeatDTO();
            roomSeatDTO.setSeatId(seat.getId());
            roomSeatDTO.setRoomId(roomId);
            roomSeatDTO.setTypeSeat(s.getTypeSeat());
            roomSeatDTO.setColumnName(s.getColumnName());
            roomSeatDTO.setRowName(s.getRowName());
            roomSeatDTOS.add(roomSeatDTO);
        }
        List<RoomSeat> roomSeats = new ArrayList<>();
        for(RoomSeatDTO rs : roomSeatDTOS){
            RoomSeat roomSeat = new RoomSeat();
            SeatRoomId seatRoomId = new SeatRoomId(rs.getRoomId(), rs.getSeatId());
            roomSeat.setId(seatRoomId);
            roomSeat.setTypeSeat(rs.getTypeSeat());

            Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
            roomSeat.setRoom(room);

            Seat seat = seatRepository.findById(rs.getSeatId()).orElseThrow(() -> new RuntimeException("Seat not found"));
            roomSeat.setSeat(seat);

            roomSeats.add(roomSeat);
        }
        roomSeatRepository.saveAll(roomSeats);

        return roomSeatDTOS;
    }

    @Override
    public List<RoomSeat> updateStatusSeat(Long roomId, List<SeatLayoutRequest> requests) {
        List<RoomSeat> roomSeats = new ArrayList<>();
        for(SeatLayoutRequest s : requests){
            Seat seat = seatRepository.findSeatsByColumnNameAndRowName(s.getColumnName(), s.getRowName());
            RoomSeat roomSeat = roomSeatRepository.findByRoomIdAndSeatId(roomId, seat.getId());
            roomSeat.setTypeSeat(s.getTypeSeat());
            roomSeats.add(roomSeat);
        }
        return roomSeatRepository.saveAll(roomSeats);
    }
}
