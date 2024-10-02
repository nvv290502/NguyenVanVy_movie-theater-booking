package com.movie_theaters.dto.request;

import com.movie_theaters.entity.enums.TypeSeat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatLayoutRequest {
    private String rowName;
    private String columnName;
    private TypeSeat typeSeat;
}
