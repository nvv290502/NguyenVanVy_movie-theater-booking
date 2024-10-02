package com.movie_theaters.repository;

import com.movie_theaters.dto.RoomDTO;
import com.movie_theaters.dto.common.RoomEarlyInitialDTO;
import com.movie_theaters.dto.common.RoomHasLayoutDTO;
import com.movie_theaters.entity.Cinema;
import com.movie_theaters.entity.Room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/*
 * Author:tungnt
 */

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT new com.movie_theaters.dto.common.RoomEarlyInitialDTO(r.id, r.name, r.location, r.screenSize, r.roomType, r.roomStatus) FROM Room r WHERE r.cinema.id = :cinemaId")
    List<RoomEarlyInitialDTO> findAllByCinemaId(@Param("cinemaId") Long cinemaId);

    @Query("SELECT r FROM Room r "+
            "WHERE r.cinema.id = :cinemaId "+
            "AND r.seatRowNumbers IS NOT NULL "+
            "AND r.seatColumnNumbers IS NOT NULL "+
            "AND r.isEnabled = TRUE")
    List<Room> findAllByCinemaIdAndRoomStatusIsAvailable(@Param("cinemaId") Long cinemaId);

    @Query("SELECT new com.movie_theaters.dto.common.RoomHasLayoutDTO(r.seatNumbers, r.seatRowNumbers, r.seatColumnNumbers, r.aislePosition, r.aisleWidth, r.aisleHeight, r.doubleSeatRowNumbers) FROM Room r WHERE r.id = :roomId")
    RoomHasLayoutDTO findRoomLayoutByRoomId(@Param("roomId") Long roomId);

    @EntityGraph(attributePaths = { "cinema" })
    Optional<Room> findById(Long roomId);

    Optional<Room> findByIdAndCinemaId(Long roomId, Long cinemaId);

    @Query("SELECT r FROM Room r " +
            "JOIN r.cinema c " +
            "JOIN r.showtimes s " +
            "JOIN s.schedule sch " +
            "WHERE sch.movie.id = :movieId AND " +
            "(sch.time = :showTime OR :showTime IS NULL) AND " +
            "(sch.date = :showDate OR :showDate IS NULL) AND " +
            "(c.id = :cinemaId OR :cinemaId IS NULL)")
    List<Room> getRoomByShowTime(Long movieId, LocalTime showTime, LocalDate showDate, Long cinemaId);
    @Query("SELECT r FROM Room r "+
            "WHERE r.cinema.id = :cinemaId")
    Page<Room> getRoomByCinema(Long cinemaId, Pageable pageable);
    @Query("SELECT r FROM Room r WHERE r.name = :name AND r.cinema.id = :cinemaId")
    Room getByName(String name, Long cinemaId);

    @Query("SELECT r.name FROM Room r WHERE r.cinema.id = :cinemaId")
    List<String> finAllCinemaNameByCinemaId(Long cinemaId);

}
