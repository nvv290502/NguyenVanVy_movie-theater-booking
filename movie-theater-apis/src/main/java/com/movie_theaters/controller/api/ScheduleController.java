package com.movie_theaters.controller.api;

import com.movie_theaters.dto.MovieDTO;
import com.movie_theaters.dto.common.ScheduleBeforeMovieLoadDTO;
import com.movie_theaters.dto.common.ScheduleEarlyDTO;
import com.movie_theaters.dto.common.ScheduleShowtimeDTO;
import com.movie_theaters.dto.response.ApiPagingResponse;
import com.movie_theaters.dto.response.ApiResponse;
import com.movie_theaters.dto.response.ScheduleManager;
import com.movie_theaters.entity.Schedule;
import com.movie_theaters.entity.Showtime;
import com.movie_theaters.service.ScheduleService;
import com.movie_theaters.service.ShowTimeService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/pub")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ShowTimeService showtimeService;

    @GetMapping("/schedule/byCinema")
    public ResponseEntity<?> getScheduleByCinema(@RequestParam(required = false) Long movieId,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) LocalDate showDate,
            @RequestParam(required = false) Long cinemaId) {
        return new ResponseEntity<>(scheduleService.getScheduleByCinema(movieId, city, showDate, cinemaId),
                HttpStatus.OK);
    }

    @GetMapping("schedule/priceTicket")
    public ResponseEntity<?> getPriceTicket(@RequestParam Long movieId,
            @RequestParam LocalDate showDate,
            @RequestParam LocalTime showTime) {
        Schedule schedule = scheduleService.getPriceTicket(movieId, showDate, showTime);
        if (schedule == null) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.NO_CONTENT, "Không có schedule!", "successfully!"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK,
                scheduleService.getPriceTicket(movieId, showDate, showTime), "successfully!"), HttpStatus.OK);
    }

    @PostMapping("schedule")
    public ResponseEntity<?> saveOrUpdateSchedule(@RequestBody ScheduleEarlyDTO scheduleEarlyDTO) {
            Boolean isExist = scheduleService.checkExistSchedule(scheduleEarlyDTO);
            Schedule schedule;

            if (Boolean.FALSE.equals(isExist)) {
                if (scheduleEarlyDTO.getScheduleId().matches("\\d+")) {
                    schedule = scheduleService.updateSchedule(scheduleEarlyDTO);
                } else {
                    schedule = scheduleService.saveSchedule(scheduleEarlyDTO);
                }
            } else {
                schedule = scheduleService.getExistSchedule(scheduleEarlyDTO);
            }

            return ResponseEntity.ok().body(schedule.getId());
    }


    @PostMapping("schedule/showtime")
    public ResponseEntity<?> saveShowtime(@RequestBody ScheduleShowtimeDTO scheduleShowtimeDTO) {
        try {
            Showtime showtime = showtimeService.saveShowtime(scheduleShowtimeDTO);
            return ResponseEntity.ok().body(showtime);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
        }
    }

    @GetMapping("schedule/{id}")
    public ResponseEntity<?> getShowtimeByRoom(@PathVariable("id") Long roomId) {
        try {
            List<ScheduleBeforeMovieLoadDTO> schedules = scheduleService.getSchedule(roomId);
            return ResponseEntity.ok().body(schedules);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
        }
    }
    @GetMapping("/schedule/manager")
    public ResponseEntity<ApiPagingResponse<ScheduleManager>> getListScheduleManager(@RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "100") int size,
                                                                               @RequestParam(defaultValue = "id") String column,
                                                                               @RequestParam(defaultValue = "desc") String sortType) {
        Page<ScheduleManager> schedules = scheduleService.getListScheduleManager(page, size, column, sortType);
        return new ResponseEntity<>(
                new ApiPagingResponse<>(HttpStatus.OK.value(), schedules, "Trả về danh sách suất chiếu thành công"),
                HttpStatus.OK);
    }
    @PutMapping("/schedule/update-price")
    public ResponseEntity<?> updatePriceTicket(@RequestParam Long scheduleId,
                                               @RequestParam Double priceTicket){
        String result = scheduleService.updatePrice(scheduleId, priceTicket);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED, null,result), HttpStatus.ACCEPTED);
    }


}
