package com.movie_theaters.dto.common;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomHasRoomSeatDTO {
    private Long id;
    private Set<RoomSeatCreateDTO> roomSeats;
}
