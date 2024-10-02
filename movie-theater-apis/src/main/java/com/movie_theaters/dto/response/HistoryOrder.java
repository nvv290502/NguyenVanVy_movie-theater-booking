package com.movie_theaters.dto.response;

import com.movie_theaters.entity.enums.StatusBill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryOrder {
    private String billCode;
    private String movieName;
    private Long movieId;
    private Long userId;
    private Integer duration;
    private String roomName;
    private LocalDate scheduleDate;
    private LocalTime scheduleTime;
    private Float amountMoney;
    private LocalDateTime createdDate;
    private StatusBill status;
    private Boolean isTicketIssued;
}
