package com.movie_theaters.repository;

import com.movie_theaters.dto.UserDTO;
import com.movie_theaters.dto.response.BillDetailHistory;
import com.movie_theaters.dto.response.HistoryOrder;
import com.movie_theaters.entity.User;
import com.movie_theaters.entity.enums.StatusBill;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u "+
            "JOIN u.roles r "+
            "WHERE r.roleName = :roleName")
    Page<User> findAll(Pageable pageable, String roleName);

    List<User> findByIsConfirmFalseAndCreatedDateBefore(LocalDateTime cutoff);

    Optional<User> findById(Long id);

    @Query("SELECT SUM(b.amountMoney) FROM Bill b "+
            "WHERE b.user.id = :userId "+
            "AND b.status = :billStatus "+
            "GROUP BY b.user.id")
    Float getAmountMoney(Long userId, StatusBill billStatus);

    @Query("SELECT DISTINCT new com.movie_theaters.dto.response.HistoryOrder(b.billCode, m.name, m.id, b.user.id, m.duration, s.room.name, sch.date, sch.time, b.amountMoney, b.createdDateTime, b.status, b.isTicketIssued) "+
            "FROM Bill b "+
            "JOIN b.billDetails bd "+
            "JOIN bd.showtime s "+
            "JOIN s.schedule sch "+
            "JOIN sch.movie m "+
            "WHERE (b.user.id = :userId OR :userId IS NULL) "+
            "AND b.status = 'PAID' "+
            "AND (sch.date = :date OR :date IS NULL) "+
            "AND (b.billCode = :billCode OR :billCode IS NULL) "+
            "ORDER BY b.createdDateTime DESC")
    Page<HistoryOrder> getHistoryBill(Long userId, LocalDate date, String billCode, Pageable pageable);

    @Query("SELECT DISTINCT u FROM User u "+
            "JOIN u.roles r "+
            "WHERE u.isConfirm = true "+
            "AND u.isEnabled = true "+
            "AND r.roleName = 'ROLE_USER' "+
            "AND u.createdDate BETWEEN :startTime AND :endTime")
    List<User> getNewCustomer(LocalDateTime startTime, LocalDateTime endTime);

}
