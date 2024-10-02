package com.movie_theaters.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "movie_id")
    private Movie movie;

    @OneToMany(mappedBy = "schedule")
    @JsonIgnore
    private Set<Showtime> showtimes = new HashSet<>();

    @Column(name = "schedule_date")
    private LocalDate date;

    @Column(name = "schedule_time")
    private LocalTime time;

    @Column(name = "time_end")
    private LocalTime timeEnd;

    @Column(name = "price_ticket")
    private Double price;

}
