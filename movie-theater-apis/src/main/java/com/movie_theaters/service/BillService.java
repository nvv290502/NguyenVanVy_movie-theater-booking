package com.movie_theaters.service;

import com.movie_theaters.dto.response.BillDetailHistory;
import com.movie_theaters.entity.Bill;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BillService {
    Bill saveBill(String billCode, Long userId, Long movieId, Long roomId, LocalDate showDate, LocalTime showTime, String seats, Float amountMoney);
    Bill updateBillByStatus(String billCode, Float amountMoney);

    void sendBill(String billCode, String email);

    String deleteBillByBillCode(String billCode);

}
