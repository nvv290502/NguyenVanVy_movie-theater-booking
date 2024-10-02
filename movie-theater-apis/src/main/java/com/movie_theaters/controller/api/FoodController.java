package com.movie_theaters.controller.api;

import com.movie_theaters.dto.response.ApiResponse;
import com.movie_theaters.entity.Food;
import com.movie_theaters.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pub")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FoodController {
    private final FoodService foodService;

    @GetMapping("/food")
    public ResponseEntity<?> getAll(){
        List<Food> foods = foodService.getAll();
        if (foods.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, foods, "Trả về danh sách food thành công!"), HttpStatus.OK);
    }
}
