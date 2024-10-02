package com.movie_theaters.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Nationalized;

import com.movie_theaters.entity.enums.RoomType;
import com.movie_theaters.entity.enums.ScreenSize;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movie_theaters.entity.enums.RoomStatus;

/*
 * Author:tungnt
 */

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column(name = "room_name")
    @Nationalized
    private String name;

    @Column(name = "location")
    @Nationalized
    private String location;

    @Column(name = "seat_numbers")
    private Integer seatNumbers;

    @Enumerated(EnumType.STRING)
    @Column(name = "screen_size")
    private ScreenSize screenSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type")
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_status")
    private RoomStatus roomStatus;

    @Column(name = "number_seat_rows")
    private Integer seatRowNumbers;

    @Column(name = "number_seat_columns")
    private Integer seatColumnNumbers;

    @Column(name = "aisle_position")
    private Integer aislePosition;

    @Column(name = "aisle_width")
    private Integer aisleWidth;

    @Column(name = "aisle_height")
    private Integer aisleHeight;

    @Column(name = "double_seat_rows")
    private Integer doubleSeatRowNumbers;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "cinema_id", referencedColumnName = "cinema_id")
    private Cinema cinema;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private Set<Showtime> showtimes = new HashSet<>();

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private Set<RoomSeat> roomSeats = new HashSet<>();

}
