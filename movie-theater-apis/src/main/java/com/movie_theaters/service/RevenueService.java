package com.movie_theaters.service;

import com.movie_theaters.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface RevenueService {
    Map<String, Object> getYearlyRevenue();
    Map<String, Object> getMonthlyRevenue(int year);
    Map<String, Object> getDailyRevenue(int year, int month);
    Map<String, Object> getHoursRevenue(int year, int month, int day);
    List<Map<String, Object>> getMovieRevenue(LocalDateTime startDate, LocalDateTime endDate);
    List<Map<String, Object>> getCinemaRevenue(LocalDateTime startDate, LocalDateTime endDate);
    List<User> getNewCustomer();
}
