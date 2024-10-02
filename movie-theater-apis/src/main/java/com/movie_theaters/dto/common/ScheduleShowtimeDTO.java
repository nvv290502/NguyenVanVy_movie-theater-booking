package com.movie_theaters.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleShowtimeDTO {
    private Long scheduleId;
    private Long roomId;
}
