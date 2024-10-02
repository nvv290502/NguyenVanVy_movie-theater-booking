package com.movie_theaters.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowTimeDTO {
    private Long cinemaId;
    private Long roomId;
    private LocalTime showTime;
    private LocalDate showDate;
    private String roomName;
    private String cinemaName;
}
