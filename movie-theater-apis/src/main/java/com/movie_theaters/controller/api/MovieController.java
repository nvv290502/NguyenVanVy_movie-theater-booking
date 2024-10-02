package com.movie_theaters.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie_theaters.dto.CategoryDTO;
import com.movie_theaters.dto.MovieDTO;
import com.movie_theaters.dto.request.MovieRequest;
import com.movie_theaters.dto.response.*;
import com.movie_theaters.entity.Category;
import com.movie_theaters.entity.Movie;
import com.movie_theaters.repository.MovieRepository;
import com.movie_theaters.service.impl.ImageUploadService;
import com.movie_theaters.service.MovieService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pub")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class MovieController {

    private final MovieService movieService;
    private final ImageUploadService imageUploadService;
    private final MovieRepository movieRepository;

    @PostMapping("/movie")
    public ResponseEntity<?> saveMovie(@ModelAttribute @Valid MovieRequest movieRequest,
            @RequestParam(value = "imageSmall", required = false) MultipartFile imageSmall,
            @RequestParam(value = "imageLarge", required = false) MultipartFile imageLarge) {
        MovieDTO movieDTO = new MovieDTO();

        System.out.println(movieRequest.getImageLargeUrl());

        try {
            if (movieRequest.getImageSmallUrl() == null) {
                movieRequest.setImageSmallUrl(imageUploadService.uploadImage(imageSmall));
            }
            if (movieRequest.getImageLargeUrl() == null) {
                movieRequest.setImageLargeUrl(imageUploadService.uploadImage(imageLarge));
            }
            BeanUtils.copyProperties(movieRequest, movieDTO);

            ObjectMapper mapper = new ObjectMapper();
            List<Long> categoryIds = mapper.readValue(movieRequest.getCategoryId(), new TypeReference<List<Long>>() {
            });
            movieDTO.setCategoryId(categoryIds);
        } catch (IOException e) {
            return new ResponseEntity<>(
                    new ErrorApiResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), e.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.CREATED, movieService.saveMovie(movieDTO), "Thêm phim thành công!"),
                HttpStatus.CREATED);
    }

    // @PostMapping("/movie")
    // public ResponseEntity<?> saveMovie(@ModelAttribute @Valid MovieRequest
    // movieRequest){
    // MovieDTO movieDTO = new MovieDTO();
    //
    // try {
    // BeanUtils.copyProperties(movieRequest, movieDTO);
    // movieDTO.setImageSmallUrl(imageUploadService.uploadImage(movieRequest.getImageSmallUrl()));
    // movieDTO.setImageLargeUrl(imageUploadService.uploadImage(movieRequest.getImageLargeUrl()));
    //
    // ObjectMapper mapper = new ObjectMapper();
    // List<Long> categoryIds = mapper.readValue(movieRequest.getCategoryId(), new
    // TypeReference<List<Long>>(){});
    // movieDTO.setCategoryId(categoryIds);
    // } catch (IOException e) {
    // return new ResponseEntity<>(new
    // ErrorApiResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(),
    // e.getMessage()), HttpStatus.NOT_FOUND);
    // }
    // return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED,
    // movieService.saveMovie(movieDTO), "Thêm phim thành công!"),
    // HttpStatus.CREATED);
    // }

    @GetMapping("/movie")
    public ResponseEntity<ApiPagingResponse<MovieDTO>> getMovie(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "id") String column,
            @RequestParam(defaultValue = "asc") String sortType) {
        Page<MovieDTO> movieDTOS = movieService.getAllMovie(page, size, column, sortType);
        return new ResponseEntity<>(
                new ApiPagingResponse<>(HttpStatus.OK.value(), movieDTOS, "Trả về danh sách phim thành công!"),
                HttpStatus.OK);
    }

    @GetMapping("/movie/isEnable")
    public ResponseEntity<ApiPagingResponse<MovieDTO>> getMovieIsEnable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "id") String column,
            @RequestParam(defaultValue = "asc") String sortType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer startDuration,
            @RequestParam(required = false) Integer endDuration,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Long categoryId) {

        Page<MovieDTO> movieDTOS = movieService.getAllMovieIsEnable(
                page, size, column, sortType, date, startDuration, endDuration, language, categoryId);

        return new ResponseEntity<>(
                new ApiPagingResponse<>(
                        HttpStatus.OK.value(),
                        movieDTOS,
                        "Trả về danh sách phim thành công"
                ),
                HttpStatus.OK
        );
    }

    @PutMapping("/movie")
    public ResponseEntity<ApiResponse<MovieDTO>> updateMovie(@ModelAttribute @Valid MovieRequest movieRequest,
            @RequestParam(value = "imageSmall", required = false) MultipartFile imageSmall,
            @RequestParam(value = "imageLarge", required = false) MultipartFile imageLarge) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED, null,
                movieService.updateMovie(movieRequest, imageSmall, imageLarge)), HttpStatus.ACCEPTED);
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable Long id) {
        if (id == null || id < 0) {
            return new ResponseEntity<>("Id không hợp lệ!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.OK, movieService.finMovieById(id), "Trả về phim theo id thành công!"),
                HttpStatus.OK);
    }

    @PostMapping("/movie/isEnable")
    public ResponseEntity<ApiResponse<String>> setIsEnable(@RequestParam Long id, @RequestParam Boolean isEnable) {
        return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.ACCEPTED, null, movieService.updateIsEnable(id, isEnable)),
                HttpStatus.ACCEPTED);
    }

    @GetMapping("/movie/getName")
    public ResponseEntity<ApiResponse<List<String>>> getListNameMovie() {
        List<Movie> movies = movieRepository.findAll();
        List<String> listName = movies.stream().map(c -> c.getName()).collect(Collectors.toList());
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, listName, "Lấy danh sách tên phim thành công!"),
                HttpStatus.OK);
    }

    @GetMapping("/movie/search")
    public ResponseEntity<ApiResponse<MovieDTO>> searchMovieByName(@RequestParam String name) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, movieService.searchMovieByName(name),
                "Trả về phim theo tên thành công!"), HttpStatus.OK);
    }

    @GetMapping("/movie/showToDay")
    public ResponseEntity<?> getMovieShowToDay() {
        List<MovieDTO> movies = movieService.getMovieShowingByDate(LocalDate.now());
        if (movies.isEmpty()) {
            return new ResponseEntity<>("Không có phim nào chiếu hôm nay!", HttpStatus.OK);
        }
        return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.OK, movies, "Trả về danh sách phim chiếu hôm nay thành công!"),
                HttpStatus.OK);
    }

    @GetMapping("/movie/upcomingReleases")
    public ResponseEntity<?> getMovieUpcomingReleases() {
        List<MovieDTO> movies = movieService.getMovieUpcomingShow(LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(7));
        if (movies.isEmpty()) {
            return new ResponseEntity<>("Không có phim nào sắp chiếu!", HttpStatus.OK);
        }
        return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.OK, movies, "Trả về danh sách phim sắp phát hành thành công!"),
                HttpStatus.OK);
    }

    @GetMapping("/movie/related")
    public ResponseEntity<?> getMovieRelated(@RequestParam List<Long> categoryIds) {
        List<Movie> movies = movieService.getMovieByCategoryId(categoryIds);
        if (movies.isEmpty()) {
            return new ResponseEntity<>("Không có phim nào liên quan!", HttpStatus.OK);
        }
        return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.OK, movies, "Trả về danh sách phim liên quan hành thành công!"),
                HttpStatus.OK);
    }

    @GetMapping("/movie/byShowTime")
    public ResponseEntity<?> getMovieByShowTime(@RequestParam LocalTime showTime,
            @RequestParam LocalDate showDate,
            @RequestParam Long cinemaId) {
        List<Movie> movies = movieService.getMovieByShowTime(showTime, showDate, cinemaId);
        if (movies.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.OK, movies, "Trả về danh sách phim theo lịch chiếu thành công!"),
                HttpStatus.OK);
    }

    @GetMapping("/movie/top-movie")
    public ResponseEntity<?> getTopMovie() {
        return ResponseEntity.ok(movieService.getTop10Movie());
    }

    @GetMapping("/movie/isShowing")
    public ResponseEntity<?> getMovieIsShowing() {
        List<MovieDTO> movieDTOs = movieService.getMovieIsShowing();
        return ResponseEntity.ok(movieDTOs);
    }
}
