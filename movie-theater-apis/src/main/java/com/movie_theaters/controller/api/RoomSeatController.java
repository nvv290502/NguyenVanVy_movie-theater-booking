package com.movie_theaters.controller.api;

import com.movie_theaters.dto.RoomSeatDTO;
import com.movie_theaters.dto.request.SeatLayoutRequest;
import com.movie_theaters.dto.response.ApiCollectionResponse;
import com.movie_theaters.dto.response.ApiResponse;
import com.movie_theaters.entity.RoomSeat;
import com.movie_theaters.service.RoomSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pub")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RoomSeatController {
    private final RoomSeatService roomSeatService;

    @GetMapping("/seats")
    public ResponseEntity<?> getSeatByRoom(@RequestParam(required = false) Long roomId){
        List<RoomSeatDTO> seats = roomSeatService.getSeatByRoom(roomId);
        if(seats.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, seats, "Trả về danh sách ghế thành công!"), HttpStatus.OK);
    }

    @PostMapping("/room/save-layout/{roomId}")
    public ResponseEntity<?> saveLayout(@PathVariable Long roomId,
                                        @RequestBody List<SeatLayoutRequest> requests){
        if (requests == null || requests.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST,null, "Danh sách ghế không thể trống"), HttpStatus.BAD_REQUEST);
        }
        List<RoomSeatDTO> roomSeatDTOS = roomSeatService.saveLayout(roomId, requests);
        return new ResponseEntity<>(new ApiCollectionResponse<>(HttpStatus.CREATED, roomSeatDTOS, "Tạo layout phòng thành công!"), HttpStatus.CREATED);
    }
    @PutMapping("/seat/update-status/{roomId}")
    public ResponseEntity<?> updateSeatStatus(@PathVariable Long roomId,
                                        @RequestBody List<SeatLayoutRequest> requests){
        if (requests == null || requests.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST,null, "Danh sách ghế không thể trống"), HttpStatus.BAD_REQUEST);
        }
        List<RoomSeat> roomSeats = roomSeatService.updateStatusSeat(roomId, requests);
        return new ResponseEntity<>(new ApiCollectionResponse<>(HttpStatus.ACCEPTED, roomSeats, "Cập nhật trạng thái ghế thành công!"), HttpStatus.ACCEPTED);
    }
}
