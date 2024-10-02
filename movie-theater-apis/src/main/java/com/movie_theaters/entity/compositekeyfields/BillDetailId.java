package com.movie_theaters.entity.compositekeyfields;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class BillDetailId implements Serializable {

    @Column(name = "bill_id")
    private Long billId;

    @Column(name = "seat_id")
    private Long seatId;

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "schedule_id")
    private Long scheduleId;

}
