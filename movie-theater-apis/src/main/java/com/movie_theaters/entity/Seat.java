package com.movie_theaters.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movie_theaters.entity.enums.TypeSeat;

@Entity
@Table(name = "seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    @Column(name = "row_name")
    private String rowName;

    @Column(name = "column_name")
    private String columnName;

    @OneToMany(mappedBy = "seat")
    @JsonIgnore
    private Set<RoomSeat> roomSeats = new HashSet<>();

    @OneToMany(mappedBy = "seat")
    @JsonIgnore
    private Set<BillDetail> billDetails = new HashSet<>();
}
