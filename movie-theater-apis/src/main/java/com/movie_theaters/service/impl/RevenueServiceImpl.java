package com.movie_theaters.service.impl;

import com.movie_theaters.entity.Movie;
import com.movie_theaters.entity.User;
import com.movie_theaters.repository.BillRepository;
import com.movie_theaters.repository.CinemaRepository;
import com.movie_theaters.repository.MovieRepository;
import com.movie_theaters.repository.UserRepository;
import com.movie_theaters.service.RevenueService;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.movie_theaters.util.IsValidDate.isValidDate;


@Service
@RequiredArgsConstructor
public class RevenueServiceImpl implements RevenueService {

    private final BillRepository billRepository;
    private final MovieRepository movieRepository;
    private final CinemaRepository cinemaRepository;
    private final UserRepository userRepository;
    @Override
    public Map<String, Object> getYearlyRevenue() {
        List<Object[]> results = billRepository.getYearlyRevenue();
        List<String> years = new ArrayList<>();
        List<String> revenues = new ArrayList<>();

        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(2);

        for (Object[] result : results) {
            years.add(result[0].toString());
            revenues.add(df.format(result[1]));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("years", years);
        response.put("revenues", revenues);

        return response;
    }

    @Override
    public Map<String, Object> getMonthlyRevenue(int year) {
        List<Object[]> results = billRepository.getMonthlyRevenue(year);

        List<String> months = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
        Map<String, Double> revenueMap = new HashMap<>();

        for (Object[] result : results) {
            revenueMap.put(result[0].toString(), (Double) result[1]);
        }

        List<String> monthList = new ArrayList<>();
        List<String> revenueList = new ArrayList<>();

        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(2);

        for (String month : months) {
            monthList.add(month);
            Double revenue = revenueMap.getOrDefault(month, 0.0);
            revenueList.add(df.format(revenue));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("month", monthList);
        response.put("revenues", revenueList);

        return response;
    }

    @Override
    public Map<String, Object> getDailyRevenue(int year, int month) {
        List<Object[]> results = billRepository.getDailyRevenue(year, month);

        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();

        Map<String, Double> revenueMap = new HashMap<>();

        for (Object[] result : results) {
            revenueMap.put(result[0].toString(), (Double) result[1]);
        }

        List<String> dayList = new ArrayList<>();
        List<String> revenueList = new ArrayList<>();

        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(2);

        for (int day = 1; day <= daysInMonth; day++) {
            String dayString = String.valueOf(day);
            dayList.add(dayString);

            Double revenue = revenueMap.getOrDefault(dayString, 0.0);
            revenueList.add(df.format(revenue));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("days", dayList);
        response.put("revenues", revenueList);

        return response;
    }

    @Override
    public Map<String, Object> getHoursRevenue(int year, int month, int day) {
        if(!isValidDate(year, month, day)){
            throw new IllegalArgumentException("Đầu vào không hợp lệ!");
        }
        List<Object[]> results = billRepository.getHoursRevenue(year, month, day);

        Map<String, Double> revenueMap = new HashMap<>();

        for (Object[] result : results) {
            revenueMap.put(result[0].toString(), (Double) result[1]);
        }

        List<String> hourList = new ArrayList<>();
        List<String> revenueList = new ArrayList<>();

        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(2);

        for (int hour = 0; hour < 24; hour++) {
            String hourString = String.valueOf(hour);
            hourList.add(hourString);

            Double revenue = revenueMap.getOrDefault(hourString, 0.0);
            revenueList.add(df.format(revenue));
        }

        // Tạo response
        Map<String, Object> response = new HashMap<>();
        response.put("hours", hourList);
        response.put("revenues", revenueList);

        return response;
    }

    public List<Map<String, Object>> getMovieRevenue(LocalDateTime startDate, LocalDateTime endDate) {

        List<Object[]> revenueResults = movieRepository.getMovieRevenue(startDate, endDate);
        List<Object[]> allMovies = movieRepository.getAllMovies();

        Map<Long, Map<String, Object>> movieMap = new HashMap<>();

        // Xử lý kết quả doanh thu
        for (Object[] row : revenueResults) {
            Map<String, Object> map = new HashMap<>();
            map.put("movieId", row[0]);
            map.put("movieName", row[1]);
            map.put("amountMoney", row[2]);
            map.put("numberTicket", row[3]);
            movieMap.put((Long) row[0], map);
        }

        // Thêm các phim chưa có doanh thu
        for (Object[] movie : allMovies) {
            Long movieId = (Long) movie[0];
            if (!movieMap.containsKey(movieId)) {
                Map<String, Object> map = new HashMap<>();
                map.put("movieId", movieId);
                map.put("movieName", movie[1]);
                map.put("amountMoney", 0.0);
                map.put("numberTicket", 0L);
                movieMap.put(movieId, map);
            }
        }
        return movieMap.values().stream()
                .sorted((map1, map2) -> {
                    Double amount1 = (Double) map1.get("amountMoney");
                    Double amount2 = (Double) map2.get("amountMoney");
                    return amount2.compareTo(amount1);
                })
                .collect(Collectors.toList());
    }
    @Override
    public List<Map<String, Object>> getCinemaRevenue(LocalDateTime startDate, LocalDateTime endDate) {

        List<Object[]> revenueResults = cinemaRepository.getCinemaRevenue(startDate, endDate);
        List<Object[]> allCinemas = cinemaRepository.getAllCinema();

        Map<Long, Map<String, Object>> cinemaMap = new HashMap<>();

        // Xử lý kết quả doanh thu
        for (Object[] row : revenueResults) {
            Map<String, Object> map = new HashMap<>();
            map.put("cinemaId", row[0]);
            map.put("cinemaName", row[1]);
            map.put("amountMoney", row[2]);
            map.put("numberTicket", row[3]);
            cinemaMap.put((Long) row[0], map);
        }

        // Thêm các phim chưa có doanh thu
        for (Object[] cinema : allCinemas) {
            Long cinemaId = (Long) cinema[0];
            if (!cinemaMap.containsKey(cinemaId)) {
                Map<String, Object> map = new HashMap<>();
                map.put("cinemaId", cinemaId);
                map.put("cinemaName", cinema[1]);
                map.put("amountMoney", 0.0);
                map.put("numberTicket", 0L);
                cinemaMap.put(cinemaId, map);
            }
        }
        return cinemaMap.values().stream()
                .sorted((map1, map2) -> {
                    Double amount1 = (Double) map1.get("amountMoney");
                    Double amount2 = (Double) map2.get("amountMoney");
                    return amount2.compareTo(amount1);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getNewCustomer() {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minus(7, ChronoUnit.DAYS);
        return userRepository.getNewCustomer(startTime, endTime);
    }
}
