package com.movie_theaters.repository;

import com.movie_theaters.dto.response.BillDetailHistory;
import com.movie_theaters.dto.response.HistoryOrder;
import com.movie_theaters.entity.Bill;
import com.movie_theaters.entity.BillDetail;
import com.movie_theaters.entity.enums.StatusBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    Boolean existsByBillDetails(BillDetail billDetail);
    @Query("SELECT b FROM Bill b WHERE b.status = :status AND :time > b.createdDateTime")
    List<Bill> findBillByStatusAndCreatedDateTimeBefore(StatusBill status, LocalDateTime time);
    Optional<Bill> findByBillCode(String billCode);
    @Query("SELECT YEAR(b.createdDateTime) as year, SUM(b.amountMoney) as revenue " +
            "FROM Bill b " +
            "GROUP BY YEAR(b.createdDateTime) " +
            "ORDER BY YEAR(b.createdDateTime) ASC")
    List<Object[]> getYearlyRevenue();
    @Query("SELECT MONTH(b.createdDateTime) as month, SUM(b.amountMoney) as revenue " +
            "FROM Bill b " +
            "WHERE YEAR (b.createdDateTime) = :year "+
            "GROUP BY MONTH(b.createdDateTime)")
    List<Object[]> getMonthlyRevenue(int year);
    @Query("SELECT DAY(b.createdDateTime) as month, SUM(b.amountMoney) as revenue " +
            "FROM Bill b " +
            "WHERE YEAR(b.createdDateTime) = :year AND MONTH(b.createdDateTime) = :month "+
            "GROUP BY DAY(b.createdDateTime)")
    List<Object[]> getDailyRevenue(int year, int month);
    @Query("SELECT HOUR(b.createdDateTime) as month, SUM(b.amountMoney) as revenue " +
            "FROM Bill b " +
            "WHERE YEAR(b.createdDateTime) = :year AND MONTH(b.createdDateTime) = :month AND DAY(b.createdDateTime) = :day "+
            "GROUP BY HOUR(b.createdDateTime)")
    List<Object[]> getHoursRevenue(int year, int month, int day);
}
