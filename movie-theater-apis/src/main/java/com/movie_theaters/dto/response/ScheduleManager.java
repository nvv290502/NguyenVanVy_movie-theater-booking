package com.movie_theaters.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleManager {
    private Long movieId;
    private Integer duration;
    private Long roomId;
    private Long cinemaId;
    private Long scheduleId;
    private Double priceTicket;
    private String movieName;
    private String roomName;
    private String cinemaName;
    private LocalDate showDate;
    private LocalTime showTime;
}
