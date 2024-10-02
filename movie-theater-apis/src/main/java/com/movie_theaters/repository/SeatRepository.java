package com.movie_theaters.repository;

import com.movie_theaters.entity.Seat;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/*
 * Author:tungnt
 */
@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findByRowNameAndColumnName(String rowName, String columnName);
    @Query("SELECT s FROM Seat s WHERE s.columnName = :columnName AND s.rowName = :rowName")
    Seat findSeatsByColumnNameAndRowName(String columnName, String rowName);
    Boolean existsByColumnNameAndRowName(String columnName, String rowName);
}
