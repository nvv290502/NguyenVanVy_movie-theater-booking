package com.movie_theaters.controller.api;

import com.movie_theaters.dto.request.BillRequest;
import com.movie_theaters.dto.response.ApiCollectionResponse;
import com.movie_theaters.dto.response.ApiResponse;
import com.movie_theaters.dto.response.BillDetailHistory;
import com.movie_theaters.entity.Bill;
import com.movie_theaters.service.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pub")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BillController {
    private final BillService billService;

    @PostMapping("/bill")
    public ResponseEntity<?> saveBill(@RequestBody @Valid BillRequest billRequest){
        Bill bill = billService.saveBill(billRequest.getBillCode(),billRequest.getUserId(), billRequest.getMovieId(), billRequest.getRoomId(), billRequest.getShowDate(), billRequest.getShowTime(), billRequest.getSeats(), billRequest.getAmountMoney());
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED, bill, "Lưu hóa đơn thành công!"), HttpStatus.OK);
    }

    @PostMapping("/bill/update-status")
    public ResponseEntity<?> updateStatus(@RequestParam String billCode,
                                          @RequestParam Float amountMoney){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED, billService.updateBillByStatus(billCode, amountMoney), "Cập nhật hóa đơn thành công!"), HttpStatus.OK);
    }

    @GetMapping("/bill/sendEmail")
    public ResponseEntity<?> sendBill(@RequestParam String billCode,
                                      @RequestParam String email){
        billService.sendBill(billCode, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/bill")
    public ResponseEntity<?> deleteBillByBillCode(@RequestParam String billCode){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, null, billService.deleteBillByBillCode(billCode)), HttpStatus.OK);
    }
}
