package com.movie_theaters.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movie_theaters.entity.compositekeyfields.SeatRoomId;
import com.movie_theaters.entity.enums.TypeSeat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room_seat")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomSeat implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private SeatRoomId id;

    @MapsId("seatId")
    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @MapsId("roomId")
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_seat")
    private TypeSeat typeSeat;
}
