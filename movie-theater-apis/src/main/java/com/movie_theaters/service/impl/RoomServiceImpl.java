package com.movie_theaters.service.impl;

import com.movie_theaters.dto.CinemaDTO;
import com.movie_theaters.dto.RoomDTO;
import com.movie_theaters.dto.SeatDTO;
import com.movie_theaters.dto.common.RoomEarlyInitialDTO;
import com.movie_theaters.dto.common.RoomHasLayoutDTO;
import com.movie_theaters.dto.common.RoomHasRoomSeatDTO;
import com.movie_theaters.dto.common.RoomSeatCreateDTO;
import com.movie_theaters.dto.request.RoomRequest;
import com.movie_theaters.entity.Cinema;
import com.movie_theaters.entity.Room;
import com.movie_theaters.entity.RoomSeat;
import com.movie_theaters.entity.Seat;
import com.movie_theaters.entity.compositekeyfields.SeatRoomId;
import com.movie_theaters.entity.enums.TypeSeat;
import com.movie_theaters.exception.EmptyListException;
import com.movie_theaters.exception.ObjNotFoundException;
import com.movie_theaters.exception.ObjectNotFoundException;
import com.movie_theaters.repository.CinemaRepository;
import com.movie_theaters.repository.RoomRepository;
import com.movie_theaters.repository.RoomSeatRepository;
import com.movie_theaters.repository.SeatRepository;
import com.movie_theaters.service.RoomService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * Author:tungnt
 */

@Service
@Transactional
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;
    private final SeatRepository seatRepository;
    private final RoomSeatRepository roomSeatRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, CinemaRepository cinemaRepository, SeatRepository seatRepository, RoomSeatRepository roomSeatRepository) {
        this.roomRepository = roomRepository;
        this.cinemaRepository = cinemaRepository;
        this.seatRepository = seatRepository;
        this.roomSeatRepository = roomSeatRepository;
    }

    @Override
    public List<RoomEarlyInitialDTO> findAllRoomByCinemaId(Long cinemaId) {
        Optional<Cinema> cinemaOpt = cinemaRepository.findById(cinemaId);
        if (!cinemaOpt.isPresent()) {
            throw new ObjNotFoundException("Rạp chiếu phim với ID " + cinemaId + " ko tồn tại.");
        }
        return roomRepository.findAllByCinemaId(cinemaOpt.get().getId());
    }

    @Override
    public Room findRoomById(Long roomId) {
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if (!roomOpt.isPresent()) {
            throw new ObjNotFoundException("Phòng với ID " + roomId + " ko tồn tại.");
        }
        return roomOpt.get();
    }

    @Override
    public Room saveRoom(RoomEarlyInitialDTO roomEarlyInitialDTO, Long cinemaId) {
        Optional<Cinema> cinemaOpt = cinemaRepository.findById(cinemaId);
        if (!cinemaOpt.isPresent()) {
            throw new ObjNotFoundException("Rạp chiếu phim với ID " + cinemaId + " ko tồn tại.");
        }
        Room room = Room.builder()
                .name(roomEarlyInitialDTO.getName())
                .location(roomEarlyInitialDTO.getLocation())
                .screenSize(roomEarlyInitialDTO.getScreenSize())
                .roomStatus(roomEarlyInitialDTO.getRoomStatus())
                .roomType(roomEarlyInitialDTO.getRoomType())
                .cinema(cinemaOpt.get())
                .build();
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(RoomHasLayoutDTO roomHasLayoutDTO, Long cinemaId, Long roomId) {
        Optional<Cinema> cinemaOpt = cinemaRepository.findById(cinemaId);
        if (cinemaOpt.isEmpty()) {
            throw new ObjNotFoundException("Rạp chiếu phim với ID " + cinemaId + " ko tồn tại.");
        }
        Optional<Room> roomOpt = roomRepository.findByIdAndCinemaId(roomId, cinemaId);
        if (roomOpt.isEmpty()) {
            throw new ObjNotFoundException("Phòng với ID " + roomId + " ko tồn tại.");
        }
        Room existingRoom = roomOpt.get();
        existingRoom.setSeatNumbers(roomHasLayoutDTO.getSeatNumbers());
        existingRoom.setSeatRowNumbers(roomHasLayoutDTO.getSeatRowNumbers());
        existingRoom.setSeatColumnNumbers(roomHasLayoutDTO.getSeatColumnNumbers());
        existingRoom.setAisleHeight(roomHasLayoutDTO.getAisleHeight());
        existingRoom.setAisleWidth(roomHasLayoutDTO.getAisleWidth());
        existingRoom.setAislePosition(roomHasLayoutDTO.getAislePosition());
        existingRoom.setDoubleSeatRowNumbers(roomHasLayoutDTO.getDoubleSeatRowNumbers());

        return roomRepository.save(existingRoom);
    }

    @Override
    public Room updateRoom(RoomEarlyInitialDTO roomEarlyInitialDTO, Long cinemaId, Long roomId) {
        Optional<Cinema> cinemaOpt = cinemaRepository.findById(cinemaId);
        if (cinemaOpt.isEmpty()) {
            throw new ObjNotFoundException("Rạp chiếu phim với ID " + cinemaId + " ko tồn tại.");
        }
        Optional<Room> roomOpt = roomRepository.findByIdAndCinemaId(roomId, cinemaId);
        if (roomOpt.isEmpty()) {
            throw new ObjNotFoundException("Phòng với ID " + roomId + " ko tồn tại.");
        }
        Room existingRoom = roomOpt.get();
        existingRoom.setName(roomEarlyInitialDTO.getName());
        existingRoom.setLocation(roomEarlyInitialDTO.getLocation());
        existingRoom.setScreenSize(roomEarlyInitialDTO.getScreenSize());
        existingRoom.setRoomStatus(roomEarlyInitialDTO.getRoomStatus());
        existingRoom.setRoomType(roomEarlyInitialDTO.getRoomType());

        return roomRepository.save(existingRoom);
    }

    @Override
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public List<RoomDTO> getRoomShowTime(Long movieId, LocalTime showTime, LocalDate showDate, Long cinemaId) {
        List<Room> rooms = roomRepository.getRoomByShowTime(movieId, showTime, showDate, cinemaId);
        return rooms.stream().map(r->{
            RoomDTO roomDTO = new RoomDTO();
            BeanUtils.copyProperties(r,roomDTO);
            return roomDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public Room saveRoom(RoomHasRoomSeatDTO roomHasRoomSeatDTO, Long roomId) {
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if (roomOpt.isEmpty()) {
            throw new ObjNotFoundException("Phòng với ID " + roomId + " ko tồn tại.");
        }
        Room room = roomOpt.get();
        for (RoomSeatCreateDTO roomSeatDTO : roomHasRoomSeatDTO.getRoomSeats()) {
            Optional<Seat> seatOpt = seatRepository.findById(roomSeatDTO.getId());
            if (seatOpt.isPresent()) {
                SeatRoomId seatRoomId = new SeatRoomId(room.getId(), seatOpt.get().getId());
                RoomSeat roomSeat = new RoomSeat();
                roomSeat.setId(seatRoomId);
                roomSeat.setSeat(seatOpt.get());
                roomSeat.setRoom(room);
                roomSeat.setTypeSeat(roomSeatDTO.getTypeSeat());
                roomSeatRepository.save(roomSeat);
            }
        }
        return roomRepository.save(room);
    }

    @Override
    public RoomHasLayoutDTO findRoomLayoutById(Long roomId) {
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if (roomOpt.isEmpty()) {
            throw new ObjNotFoundException("Phòng với ID " + roomId + " ko tồn tại.");
        }
        return roomRepository.findRoomLayoutByRoomId(roomId);
    }

    @Override
    public RoomSeatCreateDTO findRoomSeatStatus(Long roomId, SeatDTO seatDTO) {
        RoomSeat roomSeat = roomSeatRepository.findRoomSeatStatus(roomId, seatDTO.getRowName(),
                seatDTO.getColName());
        if (roomSeat == null) {
            throw new ObjNotFoundException(
                    "Không tìm thấy ghế với row " + seatDTO.getRowName() + " và cột " + seatDTO.getColName()
                            + " trong phòng có id " + roomId);
        }
        return new RoomSeatCreateDTO(roomSeat.getId(), roomSeat.getTypeSeat());
    }

    @Override
    public RoomSeatCreateDTO updateDisabledSeat(Long roomId, Long seatId, TypeSeat typeSeat) {
        RoomSeat roomSeat = roomSeatRepository.findByRoomIdAndSeatId(roomId, seatId);

        if (roomSeat == null) {
            throw new ObjNotFoundException(
                    "Không tìm thấy ghế với id " + seatId + " trong phòng có id " + roomId);
        }
        roomSeat.setTypeSeat(typeSeat);
        roomSeatRepository.save(roomSeat);
        return new RoomSeatCreateDTO(roomSeat.getSeat().getId(), roomSeat.getId(), roomSeat.getTypeSeat());
    }

    @Override
    public List<Room> findAllActiveRoomByCinemaId(Long cinemaId) {
        Optional<Cinema> cinemaOpt = cinemaRepository.findById(cinemaId);
        if (cinemaOpt.isEmpty()) {
            throw new ObjNotFoundException("Rạp chiếu phim với ID " + cinemaId + " ko tồn tại.");
        }
        return roomRepository.findAllByCinemaIdAndRoomStatusIsAvailable(cinemaOpt.get().getId());
    }

    @Override
    public Page<Room> getRoomByCinema(Long cinemaId, int page, int size, String column, String sortType) {
        Sort sort = Sort.by(column);
        if ("desc".equalsIgnoreCase(sortType)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        return roomRepository.getRoomByCinema(cinemaId, PageRequest.of(page, size, sort));
    }
    @Override
    public String updateIsEnable(Long id, Boolean isEnable) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isEmpty()) {
            throw new EmptyListException("Id không tồn tài");
        }
        room.get().setIsEnabled(isEnable);
        roomRepository.save(room.get());
        if (!isEnable) {
            return "Cập nhật trạng thái DISABLE phòng có id [" + id + "] thành công!";
        } else {
            return "Cập nhật trạng thái ENABLE phòng có id [" + id + "] thành công!";

        }
    }
    @Override
    public RoomDTO searchRoomByName(String name, Long cinemaId) {
        RoomDTO roomDTO = new RoomDTO();
        if (name.isBlank()) {
            throw new EmptyListException("Tên rạp không được để trống!");
        }
        Room room = roomRepository.getByName(name, cinemaId);
        BeanUtils.copyProperties(room, roomDTO);
        return roomDTO;
    }

    @Override
    public Room saveRoom(RoomRequest request) {
        Room room = new Room();
        BeanUtils.copyProperties(request, room);
        Optional<Cinema> cinema = cinemaRepository.findById(request.getCinemaId());
        if(cinema.isEmpty()){
            throw new ObjectNotFoundException("Rạp không tồn tại!");
        }
        room.setCinema(cinema.get());
        room.setIsEnabled(true);
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(Long roomId, RoomRequest request) {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isEmpty()){
            throw new ObjectNotFoundException("Phòng không tồn tại!");
        }
        BeanUtils.copyProperties(request, room.get());
        return roomRepository.save(room.get());
    }
    @Override
    public Room updateInitialization(Long roomId, int seatRowNumbers, int seatColumnNumbers) {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isEmpty()){
            throw new ObjectNotFoundException("Phòng không tồn tại!");
        }
        room.get().setSeatColumnNumbers(seatColumnNumbers);
        room.get().setSeatRowNumbers(seatRowNumbers);
        return roomRepository.save(room.get());
    }
}
