package com.movie_theaters.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie_theaters.dto.CinemaDTO;
import com.movie_theaters.dto.MovieDTO;
import com.movie_theaters.dto.request.CinemaRequest;
import com.movie_theaters.dto.response.ApiPagingResponse;
import com.movie_theaters.dto.response.ApiResponse;
import com.movie_theaters.dto.response.InvalidInputErrorResponse;
import com.movie_theaters.entity.Cinema;
import com.movie_theaters.entity.Movie;
import com.movie_theaters.repository.CinemaRepository;
import com.movie_theaters.service.CinemaService;
import com.movie_theaters.service.impl.CinemaServiceImpl;
import com.movie_theaters.service.impl.ImageUploadService;

import jakarta.validation.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * Author:tungnt
 */

@RestController
@RequestMapping("/api/pub")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CinemaController {

    private final CinemaService cinemaService;
    private final CinemaServiceImpl cinemaServiceImpl;
    private final CinemaRepository cinemaRepository;
    @GetMapping("/cinema")
    public ResponseEntity<?> getAllCinema() {
        try {
            List<Cinema> cinemas = cinemaService.findAllCinema();
            return ResponseEntity.ok().body(cinemas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
        }
    }

    @GetMapping("/cinema/{id}")
    public ResponseEntity<?> getCinemaById(@PathVariable("id") Long cinemaId) {
        try {
            Cinema cinemas = cinemaService.findCinemaById(cinemaId);
            return ResponseEntity.ok()
                    .body(cinemas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
        }
    }

    @PostMapping("/cinema")
    public ResponseEntity<?> createCinema(@ModelAttribute @Valid CinemaRequest request,
                                          @RequestParam(name = "imageUrl") MultipartFile imageUrl) {
        if(imageUrl == null || imageUrl.isEmpty()){
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, null, "Ảnh không được để trống!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED, cinemaService.saveCinema(request, imageUrl), "lưu rạp thành công!"), HttpStatus.CREATED);
    }

    @PutMapping("/cinema/{cinemaId}")
    public ResponseEntity<?> updateCinema(@PathVariable Long cinemaId,
                                          @ModelAttribute @Valid CinemaRequest request,
                                          @RequestParam(name = "imageUrl", required = false) MultipartFile imageUrl){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED, cinemaService.updateCinema(cinemaId, request, imageUrl), "cập nhật rạp thành công!"), HttpStatus.ACCEPTED);
    }

//    @PutMapping("/cinema/{id}/deactivate")
//    public ResponseEntity<?> deactivateCinema(@PathVariable("id") Long cinemaId) {
//        try {
//            Cinema updatedCinema = cinemaService.deactiveCinema(cinemaId);
//            return ResponseEntity.ok().body(updatedCinema.getId());
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
//        }
//    }

//    @PutMapping("/cinema/{id}/active")
//    public ResponseEntity<?> active(@PathVariable("id") Long cinemaId) {
//        try {
//            Cinema updatedCinema = cinemaService.activeCinema(cinemaId);
//            return ResponseEntity.ok().body(updatedCinema.getId());
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
//        }
//    }

    @GetMapping("/cinema/showtime")
    public ResponseEntity<?> getCinemaByMovieShowTime(@RequestParam Long movieId,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) LocalDate showDate) {
        return new ResponseEntity<>(cinemaService.getCinemaByMovieShowTime(movieId, city, showDate), HttpStatus.OK);
    }

     @GetMapping("/cinema/isEnable")
     public ResponseEntity<ApiPagingResponse<CinemaDTO>> getCinemaIsEnable(@RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "100") int size,
             @RequestParam(defaultValue = "id") String column,
             @RequestParam(defaultValue = "asc") String sortType) {
         Page<CinemaDTO> cinemaDTOS = cinemaServiceImpl.getByIsEnabled(page, size, column, sortType);
         return new ResponseEntity<>(
                 new ApiPagingResponse<>(HttpStatus.OK.value(), cinemaDTOS, "Trả về danh sách rạp thành công"),
                 HttpStatus.OK);
     }

    @GetMapping("/cinema-paging")
    public ResponseEntity<ApiPagingResponse<CinemaDTO>> getAllCinemaPaging(@RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "100") int size,
                                                                          @RequestParam(defaultValue = "id") String column,
                                                                          @RequestParam(defaultValue = "asc") String sortType) {
        Page<CinemaDTO> cinemaDTOS = cinemaServiceImpl.getAll(page, size, column, sortType);
        return new ResponseEntity<>(
                new ApiPagingResponse<>(HttpStatus.OK.value(), cinemaDTOS, "Trả về danh sách rạp thành công"),
                HttpStatus.OK);
    }

    @GetMapping("/cinema/active")
    public ResponseEntity<?> getAllActiveCinema() {
        try {
            List<Cinema> cinemas = cinemaService.findAllActiveCinema();
            return ResponseEntity.ok().body(cinemas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
        }
    }
    @GetMapping("/cinema/getName")
    public ResponseEntity<ApiResponse<List<String>>> getListNameCinema() {
        List<Cinema> cinemas = cinemaRepository.findAll();
        List<String> listName = cinemas.stream().map(Cinema::getName).collect(Collectors.toList());
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, listName, "Lấy danh sách tên rạp thành công!"),
                HttpStatus.OK);
    }
    @PostMapping("/cinema/updateStatus")
    public ResponseEntity<ApiResponse<String>> updateStatus(@RequestParam Long id, @RequestParam Boolean isEnable) {
        return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.ACCEPTED, null, cinemaService.updateIsEnable(id, isEnable)),
                HttpStatus.ACCEPTED);
    }
    @GetMapping("/cinema/search")
    public ResponseEntity<ApiResponse<CinemaDTO>> searchCinemaByName(@RequestParam String name) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, cinemaService.searchCinemaByName(name),
                "Trả về phim theo tên thành công!"), HttpStatus.OK);
    }

}
