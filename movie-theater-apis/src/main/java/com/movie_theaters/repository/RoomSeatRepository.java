package com.movie_theaters.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.movie_theaters.dto.RoomSeatDTO;
import com.movie_theaters.entity.RoomSeat;
import com.movie_theaters.entity.compositekeyfields.SeatRoomId;

@Repository
public interface RoomSeatRepository extends JpaRepository<RoomSeat, SeatRoomId> {
    @Query("SELECT rs FROM RoomSeat rs WHERE rs.room.id = :roomId AND rs.seat.rowName = :rowName AND rs.seat.columnName = :colName")
    RoomSeat findRoomSeatStatus(@Param("roomId") Long roomId, @Param("rowName") String rowName,
            @Param("colName") String colName);

    @Query("SELECT rs FROM RoomSeat rs WHERE rs.room.id = :roomId AND rs.seat.id = :seatId")
    RoomSeat findByRoomIdAndSeatId(@Param("roomId") Long roomId, @Param("seatId") Long seatId);

    @Query("SELECT new com.movie_theaters.dto.RoomSeatDTO(rs.room.id, rs.seat.id, rs.typeSeat, rs.seat.rowName, rs.seat.columnName) "+
            "FROM RoomSeat rs "+
            "WHERE rs.room.id = :roomId")
    List<RoomSeatDTO> getSeatByRoom(Long roomId);
}
