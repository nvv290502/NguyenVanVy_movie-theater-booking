package com.movie_theaters.controller.api;

import com.movie_theaters.dto.request.ReviewRequest;
import com.movie_theaters.dto.response.ApiPagingResponse;
import com.movie_theaters.dto.response.ApiResponse;
import com.movie_theaters.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pub")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/review-statistics")
    public ResponseEntity<?> getMovieReviewInfo(@RequestParam Long movieId){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, reviewService.getMovieReviewInfo(movieId), "Thống kê đánh giá phim"), HttpStatus.OK);
    }

    @PostMapping("/review")
    public ResponseEntity<?> saveReview(@RequestBody @Valid ReviewRequest request){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED, reviewService.saveReview(request), "lưu đánh giá thành công!"), HttpStatus.CREATED);
    }
    @GetMapping("/review")
    public ResponseEntity<?> getAllReview(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "6") int size,
                                          @RequestParam(required = false) Long movieId,
                                          @RequestParam(required = false) Long userId){
    return new ResponseEntity<>(new ApiPagingResponse<>(HttpStatus.OK.value(), reviewService.getReviewByMovieOrByUser(page, size, movieId, userId), "danh sách đánh giá"), HttpStatus.OK);
    }
}
