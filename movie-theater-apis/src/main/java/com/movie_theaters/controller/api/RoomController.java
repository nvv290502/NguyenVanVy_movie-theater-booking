package com.movie_theaters.controller.api;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.movie_theaters.dto.CinemaDTO;
import com.movie_theaters.dto.RoomDTO;
import com.movie_theaters.dto.common.RoomSeatCreateDTO;
import com.movie_theaters.dto.request.CinemaRequest;
import com.movie_theaters.dto.request.RoomRequest;
import com.movie_theaters.dto.response.ApiPagingResponse;
import com.movie_theaters.dto.response.ApiResponse;
import com.movie_theaters.entity.Cinema;
import com.movie_theaters.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.movie_theaters.dto.SeatDTO;
import com.movie_theaters.dto.common.RoomEarlyInitialDTO;
import com.movie_theaters.dto.common.RoomHasLayoutDTO;
import com.movie_theaters.dto.common.RoomHasRoomSeatDTO;
import com.movie_theaters.dto.response.InvalidInputErrorResponse;
import com.movie_theaters.entity.Room;
import com.movie_theaters.entity.enums.RoomStatus;
import com.movie_theaters.service.RoomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/*
 * Author:tungnt
 */

@RestController
@RequestMapping("/api/pub")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RoomController {
    private final RoomService roomService;
    private final RoomRepository roomRepository;

//    @GetMapping("/cinema/{cinemaId}/rooms")
//    public ResponseEntity<?> getAllRoomByCinema(@PathVariable("cinemaId") Long cinemaId) {
//        try {
//            List<RoomEarlyInitialDTO> rooms = roomService.findAllRoomByCinemaId(cinemaId);
//            return ResponseEntity.ok()
//                    .body(rooms);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
//        }
//    }

    @GetMapping("/room")
    public ResponseEntity<?> getRoomById(@RequestParam Long roomId) {
        try {
            Room room = roomService.findRoomById(roomId);
            return ResponseEntity.ok()
                    .body(room);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
        }
    }

//    @PostMapping("cinema/{cinemaId}/room")
//    public ResponseEntity<?> createRoomForCinema(@PathVariable("cinemaId") Long cinemaId,
//            @Valid @RequestBody RoomEarlyInitialDTO roomEarlyInitialDTO,
//            BindingResult result) {
//        try {
//            if (result.hasErrors()) {
//                Map<String, String> errors = result.getFieldErrors().stream()
//                        .collect(Collectors.toMap(
//                                fieldError -> fieldError.getField(),
//                                fieldError -> fieldError.getDefaultMessage()));
//                InvalidInputErrorResponse apiErrorResponse = new InvalidInputErrorResponse(
//                        "INVALID_INPUT",
//                        "Dữ liệu đầu vào không hợp lệ",
//                        HttpStatus.BAD_REQUEST,
//                        errors);
//                return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
//            }
//            Room newRoom = roomService.saveRoom(roomEarlyInitialDTO, cinemaId);
//            return ResponseEntity.status(HttpStatus.CREATED)
//                    .body(newRoom);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest()
//                    .body("Đầu vào không hợp lệ: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
//        }
//    }

//    @PutMapping("cinema/{cinemaId}/{id}")
//    public ResponseEntity<?> updateRoomById(@PathVariable("cinemaId") Long cinemaid,
//            @PathVariable("id") Long roomId,
//            @Valid @RequestBody RoomEarlyInitialDTO roomEarlyInitialDTO,
//            BindingResult result) {
//        try {
//            if (result.hasErrors()) {
//                Map<String, String> errors = result.getFieldErrors().stream()
//                        .collect(Collectors.toMap(
//                                fieldError -> fieldError.getField(),
//                                fieldError -> fieldError.getDefaultMessage()));
//                InvalidInputErrorResponse apiErrorResponse = new InvalidInputErrorResponse(
//                        "INVALID_INPUT",
//                        "Dữ liệu đầu vào không hợp lệ",
//                        HttpStatus.BAD_REQUEST,
//                        errors);
//                return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
//            }
//            Room updatedRoom = roomService.updateRoom(roomEarlyInitialDTO, cinemaid, roomId);
//            return ResponseEntity.ok().body(updatedRoom);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest()
//                    .body("Đầu vào không hợp lệ: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
//        }
//    }

//    @PutMapping("cinema/{cinemaId}/{id}/layout")
//    public ResponseEntity<?> updateRoomLayout(@PathVariable("cinemaId") Long cinemaid,
//            @PathVariable("id") Long roomId,
//            @RequestBody RoomHasLayoutDTO roomHasLayoutDTO) {
//        try {
//            Room updatedRoom = roomService.updateRoom(roomHasLayoutDTO, cinemaid, roomId);
//            return ResponseEntity.ok().body(updatedRoom);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest()
//                    .body("Đầu vào không hợp lệ: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
//        }
//    }

//    @GetMapping("room/{id}/layout")
//    public ResponseEntity<?> updateRoomLayout(@PathVariable("id") Long roomId) {
//        try {
//            RoomHasLayoutDTO updatedRoom = roomService.findRoomLayoutById(roomId);
//            return ResponseEntity.ok().body(updatedRoom);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
//        }
//    }

//    @GetMapping("room/{id}/seatStatus")
//    public ResponseEntity<?> getRoomSeatStatus(@PathVariable("id") Long roomId, @RequestParam("rowName") String rowName,
//            @RequestParam("colName") String colName) {
//        try {
//            RoomSeatCreateDTO roomSeatDTO = roomService.findRoomSeatStatus(roomId, new SeatDTO(rowName, colName));
//            return ResponseEntity.ok().body(roomSeatDTO);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
//        }
//    }

//    @PutMapping("room/{id}/updateSeatStatus")
//    public ResponseEntity<?> updateSeatStatus(@PathVariable("id") Long roomId, @RequestBody RoomSeatCreateDTO roomSeatDTO) {
//        try {
//            RoomSeatCreateDTO updatedRoomSeatDTO = roomService.updateDisabledSeat(roomId, roomSeatDTO.getId(),
//                    roomSeatDTO.getTypeSeat());
//            return ResponseEntity.ok().body(updatedRoomSeatDTO);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
//        }
//    }

//    @PostMapping("room/{id}")
//    public ResponseEntity<?> createRoomHasRoomSeats(@PathVariable("id") Long id,
//            @RequestBody RoomHasRoomSeatDTO roomHasRoomSeatDTO) {
//        try {
//            Room newRoom = roomService.saveRoom(roomHasRoomSeatDTO, id);
//            return ResponseEntity.status(HttpStatus.CREATED)
//                    .body(newRoom);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest()
//                    .body("Đầu vào không hợp lệ: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
//        }
//    }

//    @PutMapping("/cinema/{cinemaId}/{id}/deactivate")
//    public ResponseEntity<?> deactiveRoom(@PathVariable("id") Long id) {
//        try {
//            Room existingRoom = roomService.findRoomById(id);
//
//            existingRoom.setRoomStatus(RoomStatus.MAINTENANCE);
//
//            Room updatedRoom = roomService.saveRoom(existingRoom);
//            return ResponseEntity.ok().body(updatedRoom.getId());
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
//        }
//    }

//    @PutMapping("/cinema/{cinemaId}/{id}/active")
//    public ResponseEntity<?> active(@PathVariable("id") Long id) {
//        try {
//            Room existingRoom = roomService.findRoomById(id);
//
//            existingRoom.setRoomStatus(RoomStatus.AVAILABLE);
//
//            Room updatedRoom = roomService.saveRoom(existingRoom);
//            return ResponseEntity.ok().body(updatedRoom.getId());
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
//        }
//    }

    @PutMapping("/room/{roomId}")
    public ResponseEntity<?> updateRoom(@PathVariable Long roomId,
                                        @ModelAttribute @Valid RoomRequest request){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED, roomService.updateRoom(roomId, request), "cập nhật phòng thành công!"), HttpStatus.ACCEPTED);
    }
    @PostMapping("/room")
    public ResponseEntity<?> createRoom(@ModelAttribute @Valid RoomRequest request) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED, roomService.saveRoom(request), "lưu phòng thành công!"), HttpStatus.CREATED);
    }

    @GetMapping("/room/showtime")
    public ResponseEntity<ApiResponse<List<RoomDTO>>> getRoomByMovieShow(@RequestParam Long movieId,
                                                                         @RequestParam(required = false) LocalTime showTime,
                                                                         @RequestParam(required = false) LocalDate showDate,
                                                                         @RequestParam(required = false) Long cinemaId ){
        List<RoomDTO> roomDTOS = roomService.getRoomShowTime(movieId, showTime, showDate, cinemaId);
        if(roomDTOS.isEmpty()){
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, null, "Không có phòng nào có suất chiếu!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, roomDTOS, "Trả về danh sách phòng có suất chiếu thành công!"), HttpStatus.OK);
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<?> findRoomById(@PathVariable("id") Long id) {
        try {
            Room room = roomService.findRoomById(id);
            return ResponseEntity.ok()
                    .body(room);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Server error: " + e.getMessage());
        }
    }

    @GetMapping("/cinema/{cinemaId}/rooms/active")
    public ResponseEntity<?> getAllActiveRoomByCinema(@PathVariable("cinemaId") Long cinemaId) {
        try {
            List<Room> rooms = roomService.findAllActiveRoomByCinemaId(cinemaId);
            return ResponseEntity.ok()
                    .body(rooms);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
        }
    }
    @GetMapping("/room/by-cinema")
    public ResponseEntity<?> getRoomByCinema(@RequestParam Long cinemaId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "100") int size,
                                             @RequestParam(defaultValue = "id") String column,
                                             @RequestParam(defaultValue = "asc") String sortType){
        Page<Room> rooms = roomService.getRoomByCinema(cinemaId, page, size, column, sortType);
        return new ResponseEntity<>(new ApiPagingResponse<>(HttpStatus.OK.value(), rooms, "Danh sách phòng theo rạp!"), HttpStatus.OK);
    }
    @GetMapping("/room/getName")
    public ResponseEntity<ApiResponse<List<String>>> getListNameRoom(@RequestParam Long cinemaId) {
        List<String> listName = roomRepository.finAllCinemaNameByCinemaId(cinemaId);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, listName, "Lấy danh sách tên phòng thành công!"),
                HttpStatus.OK);
    }
    @PostMapping("/room/updateEnabled")
    public ResponseEntity<ApiResponse<String>> updateEnabled(@RequestParam Long id, @RequestParam Boolean isEnable) {
        return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.ACCEPTED, null, roomService.updateIsEnable(id, isEnable)),
                HttpStatus.ACCEPTED);
    }
    @GetMapping("/room/search")
    public ResponseEntity<ApiResponse<RoomDTO>> searchRoomByName(@RequestParam String name,
                                                                 @RequestParam Long cinemaId) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, roomService.searchRoomByName(name, cinemaId),
                "Trả về phòng theo tên thành công!"), HttpStatus.OK);
    }
    @PostMapping("/room/update-initialization")
    public ResponseEntity<?> updateInitialization(@RequestParam Long roomId,
                                                  @RequestParam int seatRowNumbers,
                                                  @RequestParam int seatColumnNumbers){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED, roomService.updateInitialization(roomId, seatRowNumbers, seatColumnNumbers), "khởi tạo phòng thành công!"), HttpStatus.ACCEPTED);
    }
}