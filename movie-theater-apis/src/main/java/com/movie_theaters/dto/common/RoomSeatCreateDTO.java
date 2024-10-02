package com.movie_theaters.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.movie_theaters.entity.compositekeyfields.SeatRoomId;
import com.movie_theaters.entity.enums.TypeSeat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomSeatCreateDTO {
    private Long id;
    private SeatRoomId srId;
    private TypeSeat typeSeat;

    public RoomSeatCreateDTO(SeatRoomId srId, TypeSeat typeSeat) {
        this.srId = srId;
        this.typeSeat = typeSeat;
    }

}
