package com.movie_theaters.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomHasLayoutDTO {
    private Integer seatNumbers;
    private Integer seatRowNumbers;
    private Integer seatColumnNumbers;
    private Integer aislePosition;
    private Integer aisleWidth;
    private Integer aisleHeight;
    private Integer doubleSeatRowNumbers;
}
