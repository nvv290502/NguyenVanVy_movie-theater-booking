package com.movie_theaters.controller.api;

import com.movie_theaters.dto.response.ApiCollectionResponse;
import com.movie_theaters.dto.response.ApiResponse;
import com.movie_theaters.dto.response.BillDetailHistory;
import com.movie_theaters.service.BillDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pub/")
@CrossOrigin(origins = "*")
public class BillDetailController {

    private final BillDetailService billDetailService;

    @GetMapping("/seatByBillDetail")
    public ResponseEntity<?> getSeatByBillDetail(@RequestParam Long movieId,
                                                 @RequestParam Long roomId,
                                                 @RequestParam LocalDate showDate,
                                                 @RequestParam LocalTime showTime,
                                                 @RequestParam(required = false) Long userId){
        List<Long> seatIds = billDetailService.getSeatByBillDetail(movieId, roomId, showDate, showTime, userId);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, seatIds, "Trả về danh sách ghế theo hóa đơn thành công!"), HttpStatus.OK);
    }

    @GetMapping("/bill-detail")
    public ResponseEntity<?> getBillDetail(@RequestParam String billCode){
        List<BillDetailHistory> bills = billDetailService.getBillDetail(billCode);
        return new ResponseEntity<>(new ApiCollectionResponse<>(HttpStatus.OK, bills, "Danh sách vé theo hóa đơn!"), HttpStatus.OK);
    }
}
