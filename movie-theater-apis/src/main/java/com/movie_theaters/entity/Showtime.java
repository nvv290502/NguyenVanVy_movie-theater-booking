package com.movie_theaters.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movie_theaters.entity.compositekeyfields.ShowtimeId;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "showtimes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Showtime implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ShowtimeId id;

    @ManyToOne
    @JoinColumn(name = "schedule_id", insertable = false, updatable = false)
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private Room room;

    @OneToMany(mappedBy = "showtime")
    @JsonIgnore
    private Set<BillDetail> billDetails = new HashSet<>();
}
