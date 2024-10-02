package com.movie_theaters.controller.api;

import com.movie_theaters.dto.ShowTimeDTO;
import com.movie_theaters.dto.response.ApiResponse;
import com.movie_theaters.service.ShowTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pub")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ShowTimeController {

    private final ShowTimeService service;

    @GetMapping("/showtime")
    public ResponseEntity<?> getShowTimeByMovie(@RequestParam Long movieId){
        List<ShowTimeDTO> showTimeDTOS = service.getShowTimeByMovie(movieId);
        if(showTimeDTOS.isEmpty()){
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.NO_CONTENT, null,"Không có suất chiếu nào!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, showTimeDTOS, "Trả về danh sách suất chiếu thanh công!"), HttpStatus.OK);
    }
    @DeleteMapping("/showtime")
    public ResponseEntity<?> deleteShowtime(@RequestParam Long scheduleId,
                                            @RequestParam Long roomId){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED, null, service.deleteShowtime(scheduleId, roomId)), HttpStatus.ACCEPTED);
    }

}
