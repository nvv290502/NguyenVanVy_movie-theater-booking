package com.movie_theaters.service;

import com.movie_theaters.dto.common.ScheduleBeforeMovieLoadDTO;
import com.movie_theaters.dto.common.ScheduleEarlyDTO;
import com.movie_theaters.dto.response.ScheduleManager;
import com.movie_theaters.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ScheduleService {
    List<Schedule> getScheduleByCinema(Long movieId, String city, LocalDate date, Long cinemaId);

    Schedule getPriceTicket(Long movieId, LocalDate showDate, LocalTime showTime);

    Schedule saveSchedule(ScheduleEarlyDTO scheduleEarlyDTO);
    Schedule updateSchedule(ScheduleEarlyDTO scheduleEarlyDTO);

    Boolean checkExistSchedule(ScheduleEarlyDTO scheduleEarlyDTO);

    Schedule getExistSchedule(ScheduleEarlyDTO scheduleEarlyDTO);

    List<ScheduleBeforeMovieLoadDTO> getSchedule(Long id);
    Page<ScheduleManager> getListScheduleManager(int page, int size, String column, String sortType);
    String updatePrice(Long scheduleId, Double price);

}
