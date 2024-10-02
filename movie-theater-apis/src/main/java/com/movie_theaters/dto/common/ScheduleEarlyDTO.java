package com.movie_theaters.dto.common;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleEarlyDTO {
    private String scheduleId;
    private Long roomId;
    private Long movieId;
    private LocalDate date;
    private LocalTime time;
    private LocalTime timeEnd;
}
