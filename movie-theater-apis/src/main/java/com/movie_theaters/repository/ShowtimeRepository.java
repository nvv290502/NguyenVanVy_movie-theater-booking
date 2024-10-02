package com.movie_theaters.repository;

import com.movie_theaters.dto.ShowTimeDTO;
import com.movie_theaters.entity.Room;
import com.movie_theaters.entity.Schedule;
import com.movie_theaters.entity.Showtime;
import com.movie_theaters.entity.compositekeyfields.ShowtimeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, ShowtimeId> {
    @Query("SELECT new com.movie_theaters.dto.ShowTimeDTO(c.id, r.id, sch.time, sch.date, r.name, c.name) FROM Showtime s "+
            "JOIN s.room r "+
            "JOIN r.cinema c "+
            "JOIN s.schedule sch "+
            "WHERE sch.movie.id = :movieId AND "+
            "sch.date >= :currentDate")
    List<ShowTimeDTO> getShowTimeByMovie(Long movieId, LocalDate currentDate);

    Showtime getShowtimesByRoomAndSchedule(Room room, Schedule schedule);
}
