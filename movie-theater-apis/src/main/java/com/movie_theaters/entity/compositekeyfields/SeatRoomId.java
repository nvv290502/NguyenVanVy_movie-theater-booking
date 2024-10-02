package com.movie_theaters.entity.compositekeyfields;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class SeatRoomId implements Serializable {
    private Long roomId;
    private Long seatId;
}
