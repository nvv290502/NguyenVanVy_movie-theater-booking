package com.movie_theaters.entity.compositekeyfields;

import java.io.Serializable;

import jakarta.persistence.Column;
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
public class ShowtimeId implements Serializable {
    @Column(name = "schedule_id")
    private Long scheduleId;
    @Column(name = "room_id")
    private Long roomId;
}
