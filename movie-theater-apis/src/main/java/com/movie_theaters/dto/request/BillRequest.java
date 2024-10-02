package com.movie_theaters.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillRequest {

    @NotBlank(message = "bill code không được null")
    private String billCode;
    @NotNull(message = "user id không được null")
    private Long userId;
    @NotNull(message = "ngày chiếu không được null")
    private LocalDate showDate;
    @NotNull(message = "giờ chiếu không được null")
    private LocalTime showTime;
    @NotNull(message = "movie id không được null")
    private Long movieId;
    @NotNull(message = "room id không được null")
    private Long roomId;
    @NotBlank(message = "Vui lòng chọn ghế!")
    private String seats;
    @NotNull(message = "Số tiền không hợp lệ!")
    private Float amountMoney;
}
