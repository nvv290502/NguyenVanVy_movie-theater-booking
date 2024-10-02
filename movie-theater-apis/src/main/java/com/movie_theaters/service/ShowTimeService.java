package com.movie_theaters.service;

import com.movie_theaters.dto.ShowTimeDTO;
import com.movie_theaters.dto.common.ScheduleShowtimeDTO;
import com.movie_theaters.entity.Showtime;

import java.util.List;

public interface ShowTimeService {
    List<ShowTimeDTO> getShowTimeByMovie(Long movieId);

    Showtime saveShowtime(ScheduleShowtimeDTO scheduleShowtimeDTO);
    String deleteShowtime(Long scheduleId, Long roomId);

}
