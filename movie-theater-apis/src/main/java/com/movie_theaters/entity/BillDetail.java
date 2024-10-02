package com.movie_theaters.entity;

import com.movie_theaters.entity.compositekeyfields.BillDetailId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "bill_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private BillDetailId id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "schedule_id", referencedColumnName = "schedule_id", insertable = false, updatable = false),
            @JoinColumn(name = "room_id", referencedColumnName = "room_id", insertable = false, updatable = false)
    })
    private Showtime showtime;

    @ManyToOne
    @JoinColumn(name = "bill_id", insertable = false, updatable = false)
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "seat_id", insertable = false, updatable = false)
    private Seat seat;
}

