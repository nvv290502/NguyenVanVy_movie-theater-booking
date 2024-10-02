package com.movie_theaters.dto.common;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleBeforeMovieLoadDTO {
    private Long scheduleId;
    private Long movieId;
    private String movieName;
    private String movieSmallImageUrl;
    private LocalDate date;
    private LocalTime time;
    private LocalTime timeEnd;
}
