package com.movie_theaters.controller.api;

import com.movie_theaters.dto.response.ApiCollectionResponse;
import com.movie_theaters.dto.response.ApiResponse;
import com.movie_theaters.entity.User;
import com.movie_theaters.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pub")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RevenueController {
    private final RevenueService revenueService;
    @GetMapping("/revenue/yearly")
    public ResponseEntity<Map<String, Object>> getYearlyRevenue() {
        Map<String, Object> yearlyRevenue = revenueService.getYearlyRevenue();
        return ResponseEntity.ok(yearlyRevenue);
    }
    @GetMapping("/revenue/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlyRevenue(@RequestParam int year) {
        Map<String, Object> monthlyRevenue = revenueService.getMonthlyRevenue(year);
        return ResponseEntity.ok(monthlyRevenue);
    }

    @GetMapping("/revenue/daily")
    public ResponseEntity<Map<String, Object>> getDailyRevenue(@RequestParam int year,
                                                               @RequestParam int month) {
        Map<String, Object> dailyRevenue = revenueService.getDailyRevenue(year, month);
        return ResponseEntity.ok(dailyRevenue);
    }
    @GetMapping("/revenue/hours")
    public ResponseEntity<Map<String, Object>> getHoursRevenue(@RequestParam int year,
                                                               @RequestParam int month,
                                                               @RequestParam int day) {
        Map<String, Object> hoursRevenue = revenueService.getHoursRevenue(year, month, day);
        return ResponseEntity.ok(hoursRevenue);
    }
    @GetMapping("/revenue/movie")
    public ResponseEntity<?> getMovieRevenue(@RequestParam(required = false) LocalDate startDate,
                                             @RequestParam(required = false) LocalDate endDate){

        try {
            LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
            LocalDateTime endDateTime = (endDate != null) ? endDate.atStartOfDay() : null;

            List<Map<String, Object>> movieRevenue = revenueService.getMovieRevenue(startDateTime, endDateTime);
            return ResponseEntity.ok(movieRevenue);

        } catch (DateTimeException e) {
            return ResponseEntity.badRequest().body("Có lỗi khi chuyển đổi dữ liệu!");
        }
    }
    @GetMapping("/revenue/cinema")
    public ResponseEntity<?> getCinemaRevenue(@RequestParam(required = false) LocalDate startDate,
                                              @RequestParam(required = false) LocalDate endDate){

        try {
            LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
            LocalDateTime endDateTime = (endDate != null) ? endDate.atStartOfDay() : null;

            List<Map<String, Object>> cinemaRevenue = revenueService.getCinemaRevenue(startDateTime, endDateTime);
            return ResponseEntity.ok(cinemaRevenue);

        } catch (DateTimeException e) {
            return ResponseEntity.badRequest().body("Có lỗi khi chuyển đổi dữ liệu!");
        }
    }
    @GetMapping("/revenue/new-customer")
    public ResponseEntity<?> getNewCustomer() {
        List<User> users = revenueService.getNewCustomer();
        return new ResponseEntity<>(new ApiCollectionResponse<>(HttpStatus.OK, users, "Danh sách khách hàng mới!"), HttpStatus.OK);
    }
}
