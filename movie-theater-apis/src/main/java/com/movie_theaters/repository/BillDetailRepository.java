package com.movie_theaters.repository;

import com.movie_theaters.dto.response.BillDetailHistory;
import com.movie_theaters.entity.BillDetail;
import com.movie_theaters.entity.compositekeyfields.BillDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, BillDetailId> {

    @Query("SELECT DISTINCT bd.seat.id FROM BillDetail bd " +
            "JOIN bd.showtime s " +
            "JOIN s.schedule sch " +
            "WHERE sch.movie.id = :movieId " +
            "AND sch.date = :showDate " +
            "AND sch.time = :showTime " +
            "AND bd.showtime.room.id = :roomId " +
            "AND (:userId IS NULL OR bd.bill.user.id = :userId)")
    List<Long> getSeatByBillDetail(Long movieId, Long roomId, LocalDate showDate, LocalTime showTime, Long userId);

    @Query("SELECT bd FROM BillDetail bd " +
            "WHERE bd.showtime.room.id = :roomId " +
            "AND bd.seat.id = :seatId " +
            "AND bd.showtime.schedule.id = :scheduleId")
    BillDetail getBillDetailByRoomAndSeatAndSchedule(Long roomId, Long seatId, Long scheduleId);

    @Query("SELECT bd FROM BillDetail bd WHERE bd.bill.id = :billId")
    List<BillDetail> findByBillId(Long billId);

    @Query("SELECT DISTINCT new com.movie_theaters.dto.response.BillDetailHistory(bd.showtime.room.id, bd.showtime.room.name, bd.showtime.room.cinema.name, bd.seat.id, bd.seat.columnName, bd.seat.rowName, bd.showtime.schedule.price) "+
            "FROM BillDetail bd "+
            "WHERE bd.bill.billCode =:billCode")
    List<BillDetailHistory> getBillDetail(String billCode);
    @Query("SELECT sch.movie.id, sch.movie.name, COUNT(*) as numberTicket FROM BillDetail bd "+
            "JOIN bd.showtime s "+
            "JOIN s.schedule sch "+
            "GROUP BY sch.movie.id, sch.movie.name")
    List<Object> getNumberTicketByMovie();

}
