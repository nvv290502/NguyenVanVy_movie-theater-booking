package com.movie_theaters.dto;

import com.movie_theaters.entity.enums.TypeSeat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomSeatDTO {
    private Long roomId;
    private Long seatId;
    private TypeSeat typeSeat;
    private String rowName;
    private String columnName;
}
