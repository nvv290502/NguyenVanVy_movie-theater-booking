package com.movie_theaters.dto.response;

import com.movie_theaters.entity.enums.TypeSeat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BillDetailHistory {
    private Long roomId;
    private String roomName;
    private String cinemaName;
    private Long seatId;
    private String columnName;
    private String rowName;
    private Double priceTicket;
    private TypeSeat typeSeat;

    public BillDetailHistory(Long roomId, String roomName, String cinemaName, Long seatId, String columnName, String rowName, Double priceTicket) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.cinemaName = cinemaName;
        this.seatId = seatId;
        this.columnName = columnName;
        this.rowName = rowName;
        this.priceTicket = priceTicket;
    }
}
