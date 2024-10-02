package com.movie_theaters.controller.api;

import com.movie_theaters.dto.request.BillFoodRequest;
import com.movie_theaters.dto.response.ApiCollectionResponse;
import com.movie_theaters.service.BillFoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pub")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BillFoodController {
    private final BillFoodService billFoodService;

    @PostMapping("/bill-food")
    public ResponseEntity<?> saveBillFoods(@RequestBody BillFoodRequest request) {
        return new ResponseEntity<>(billFoodService.saveBillFood(request), HttpStatus.ACCEPTED);
    }

    @GetMapping("/bill-food-detail")
    public ResponseEntity<?> getBillFoodDetail(@RequestParam String billCode){
        return new ResponseEntity<>(new ApiCollectionResponse<>(HttpStatus.OK, billFoodService.getBillFoodDetail(billCode),"Danh sách hóa đơn đồ ăn!"), HttpStatus.OK);
    }
}
