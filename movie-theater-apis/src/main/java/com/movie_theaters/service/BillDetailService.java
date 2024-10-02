package com.movie_theaters.service;

import com.movie_theaters.dto.response.BillDetailHistory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface BillDetailService {
    List<Long> getSeatByBillDetail(Long movieId, Long roomId, LocalDate showDate, LocalTime showTime, Long userId);
    List<BillDetailHistory> getBillDetail(String billCode);
    List<Map<String, Object>> getMovieTicketStats();

}
