package com.movie_theaters.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie_theaters.dto.MovieDTO;
import com.movie_theaters.dto.request.MovieRequest;
import com.movie_theaters.dto.response.MovieRating;
import com.movie_theaters.dto.response.MovieSales;
import com.movie_theaters.entity.Category;
import com.movie_theaters.entity.Movie;
import com.movie_theaters.entity.enums.StatusMovie;
import com.movie_theaters.exception.EmptyListException;
import com.movie_theaters.exception.ExistObjectException;
import com.movie_theaters.repository.CategoryRepository;
import com.movie_theaters.repository.MovieRepository;
import com.movie_theaters.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final CategoryRepository categoryRepository;
    private final ImageUploadService imageUploadService;

    @Override
    public MovieDTO saveMovie(MovieDTO movieDTO) {
        if (movieRepository.existsByNameAndSummary(movieDTO.getName(), movieDTO.getSummary())) {
            throw new ExistObjectException("Phim đã tồn tại");
        }
        Movie movie = new Movie();
        BeanUtils.copyProperties(movieDTO, movie);
        movie.setIsEnable(true);
        movie.setStatus(StatusMovie.SAP_CHIEU);
        List<Category> categories = new ArrayList<>();
        List<Long> categoryId = movieDTO.getCategoryId();
        for (Long id : categoryId) {
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isPresent()) {
                categories.add(category.get());
            } else {
                throw new IllegalArgumentException("category không tồn tại!");
            }
        }
        movie.setCategories(new HashSet<>(categories));
        movieRepository.save(movie);
        return movieDTO;
    }

    @Override
    public Page<MovieDTO> getAllMovie(int page, int size, String column, String sortType) {
        Sort sort = Sort.by(column);
        if ("asc".equalsIgnoreCase(sortType)) {
            sort = sort.ascending();
        } else {
            sort = sort.descending();
        }
        Page<Movie> movies = movieRepository.findAll(PageRequest.of(page, size, sort));
        if (movies.isEmpty()) {
            throw new EmptyListException("Không có phim nào");
        }
        return movies.map(this::convertToDTO);
    }

    @Override
    public Page<MovieDTO> getAllMovieIsEnable(int page, int size, String column, String sortType,
                                              LocalDate date, Integer startDuration, Integer endDuration,
                                              String language, Long categoryId) {
        Sort sort = Sort.by(column);
        if ("desc".equalsIgnoreCase(sortType)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Page<Movie> movieByFilters = movieRepository.getMovieByFilters(
                PageRequest.of(page, size, sort),
                date,
                startDuration,
                endDuration,
                language,
                categoryId,
                true
        );

        return movieByFilters.map(this::convertToDTO);
    }

    private MovieDTO convertToDTO(Movie movie) {
        MovieDTO movieDTO = new MovieDTO();
        BeanUtils.copyProperties(movie, movieDTO);
        if (movie.getStatus() != null) {
            movieDTO.setStatus(movie.getStatus().getDisplayName());
        }
        movieDTO.setCategoryId(
                movie.getCategories().stream()
                        .map(Category::getId)
                        .collect(Collectors.toList()));
        return movieDTO;
    }

    @Override
    public String updateMovie(MovieRequest movieRequest, MultipartFile imageSmall, MultipartFile imageLarge) {
        if (movieRequest.getId() == null) {
            throw new NullPointerException("Bạn chưa nhập id");
        }
        Optional<Movie> movie = movieRepository.findById(movieRequest.getId());
        if (movie.isEmpty()) {
            throw new EmptyListException("Phim không tồn tại!");
        }
        try {
            if (movieRequest.getImageSmallUrl() == null) {
                movieRequest.setImageSmallUrl(imageUploadService.uploadImage(imageSmall));
            }
            if (movieRequest.getImageLargeUrl() == null) {
                movieRequest.setImageLargeUrl(imageUploadService.uploadImage(imageLarge));
            }
            BeanUtils.copyProperties(movieRequest, movie.get());

            ObjectMapper mapper = new ObjectMapper();
            Set<Category> categories = mapper.readValue(movieRequest.getCategoryId(), new TypeReference<>() {
            });
            movie.get().setCategories(categories);
            movieRepository.save(movie.get());
        } catch (IOException ex) {
            log.error("lỗi khi xử lý ảnh");
        }
        return "Cập nhật phim thành công!";
    }

    @Override
    public MovieDTO finMovieById(Long id) {
        MovieDTO movieDTO = new MovieDTO();
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isEmpty()) {
            throw new EmptyListException("Phim không tồn tại!");
        }
        BeanUtils.copyProperties(movie.get(), movieDTO);
        if (movie.get().getStatus() != null) {
            movieDTO.setStatus(movie.get().getStatus().getDisplayName());
        }
        movieDTO.setCategoryId(
                movie.get().getCategories().stream().map(Category::getId).collect(Collectors.toList()));
        return movieDTO;
    }

    @Override
    public String updateIsEnable(Long id, Boolean isEnable) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isEmpty()) {
            throw new EmptyListException("Id không tồn tài");
        }
        movie.get().setIsEnable(isEnable);
        movieRepository.save(movie.get());
        if (!isEnable) {
            return "Cập nhật trạng thái DISABLE phim có id [" + id + "] thành công!";
        } else {
            return "Cập nhật trạng thái ENABLE phim có id [" + id + "] thành công!";

        }
    }

    @Override
    public MovieDTO searchMovieByName(String name) {
        MovieDTO movieDTO = new MovieDTO();
        if (name.isBlank()) {
            throw new EmptyListException("Tên thể phim không được để trống!");
        }
        Movie movie = movieRepository.getByName(name);
        BeanUtils.copyProperties(movie, movieDTO);
        return movieDTO;
    }

    @Override
    public List<MovieDTO> getMovieShowingByDate(LocalDate date) {

        List<Movie> movies = movieRepository.getMovieShowingByDate(date);
        return movies.stream().map(m -> {
            MovieDTO movieDTO = new MovieDTO();
            BeanUtils.copyProperties(m, movieDTO);
            return movieDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> getMovieUpcomingShow(LocalDate startDate, LocalDate endDate) {

        List<Movie> movies = movieRepository.getMovieUpcomingShow(startDate, endDate);
        return movies.stream().map(m -> {
            MovieDTO movieDTO = new MovieDTO();
            BeanUtils.copyProperties(m, movieDTO);
            return movieDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Movie> getMovieByCategoryId(List<Long> categoryIds) {
        if (categoryIds.isEmpty()) {
            throw new NullPointerException("Danh sách thể loại không được null!");
        }
        return movieRepository.getMovieByListCategoryId(categoryIds);
    }

    @Override
    public List<Movie> getMovieByShowTime(LocalTime showTime, LocalDate showDate, Long cinemaId) {
        return movieRepository.getMovieByShowTime(showTime, showDate, cinemaId);
    }

    @Override
    public Map<Long, Double> getTop10Movie() {
        List<MovieSales> movieSales = movieRepository.getTop10MovieByNumberTicket();
        List<MovieRating> movieRatings = movieRepository.getTop10MovieByRating();

        int maxSales = movieSales.stream().mapToInt(MovieSales::getNumberTicket).max().orElse(1);
        double maxRating = movieRatings.stream().mapToDouble(MovieRating::getRating).max().orElse(1.0);

        Map<Long, Integer> salesMap = new HashMap<>();
        for (MovieSales sales : movieSales) {
            salesMap.put(sales.getMovieId(), sales.getNumberTicket());
        }

        Map<Long, Double> ratingMap = new HashMap<>();
        for (MovieRating rating : movieRatings) {
            ratingMap.put(rating.getMovieId(), Double.valueOf(rating.getRating()));
        }

        Set<Long> allMovieIds = new HashSet<>();
        allMovieIds.addAll(salesMap.keySet());
        allMovieIds.addAll(ratingMap.keySet());
        DecimalFormat df = new DecimalFormat("#.##");
        Map<Long, Double> hotScoreMap = new HashMap<>();
        for (Long movieId : allMovieIds) {
            Integer sales = salesMap.getOrDefault(movieId, 0);
            Double rating = ratingMap.getOrDefault(movieId, 0.0);

            double normalizedSales = sales != null ? Math.log(sales + 1) / Math.log(maxSales + 1) : 0;
            double normalizedRating = rating != null ? rating / maxRating : 0;

            double hotScore = (0.6 * normalizedSales) + (0.4 * normalizedRating);
            hotScoreMap.put(movieId, Double.valueOf(df.format(hotScore * 10)));
        }

        // Sắp xếp theo điểm hotScore từ cao đến thấp và lấy 10 bộ phim đầu tiên
        return hotScoreMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    public List<MovieDTO> getMovieIsShowing() {
        List<MovieDTO> movieDTOs = new ArrayList<>();
        List<Movie> movies = movieRepository.findByStatusOrderByReleasedDateDesc(StatusMovie.DANG_CHIEU);
        for (Movie movie : movies) {
            MovieDTO movieDTO = new MovieDTO();
            BeanUtils.copyProperties(movie, movieDTO);
            movieDTOs.add(movieDTO);
        }
        return movieDTOs;
    }

    @Scheduled(cron = "0 * * * * *")
    public void updateMovieStatusesDaily() {
        List<Movie> movies = movieRepository.findAll();
        LocalDate today = LocalDate.now();
        for (Movie movie : movies) {
            if (movie.getReleasedDate().isEqual(today) && !StatusMovie.DANG_CHIEU.equals(movie.getStatus())) {
                movie.setStatus(StatusMovie.DANG_CHIEU);
                log.info("Cập nhật phim sắp chiếu {}", movie);
                movieRepository.save(movie);
            }
        }
    }
}
