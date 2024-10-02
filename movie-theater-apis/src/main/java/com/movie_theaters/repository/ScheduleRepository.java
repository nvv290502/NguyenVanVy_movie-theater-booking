package com.movie_theaters.repository;

import com.movie_theaters.dto.common.ScheduleBeforeMovieLoadDTO;
import com.movie_theaters.dto.response.ScheduleManager;
import com.movie_theaters.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
        @Query("SELECT sch FROM Schedule sch " +
                "JOIN sch.showtimes s " +
                "JOIN s.room r " +
                "JOIN r.cinema c " +
                "WHERE (sch.movie.id = :movieId OR :movieId IS NULL) AND " +
                "(c.address LIKE %:city% OR :city IS NULL) AND " +
                "(sch.date = :showDate OR :showDate IS NULL) AND " +
                "(c.id = :cinemaId OR :cinemaId IS NULL) AND " +
                "(sch.date >= :currentDate) " +
                "ORDER BY sch.time ASC")
        List<Schedule> getScheduleByCinema(Long movieId, String city, LocalDate showDate, Long cinemaId,
                        LocalDate currentDate);

        @Query("SELECT sch FROM Schedule sch " +
                        "WHERE sch.movie.id = :movieId AND sch.time = :showTime AND sch.date = :showDate")
        Schedule getScheduleByMovieAndShowDateAndSowTime(Long movieId, LocalDate showDate, LocalTime showTime);

        Boolean existsByMovieIdAndDateAndTime(Long movieId, LocalDate date, LocalTime time);

        Schedule findByMovieIdAndDateAndTime(Long movieId, LocalDate date, LocalTime time);

        @Query("SELECT new com.movie_theaters.dto.common.ScheduleBeforeMovieLoadDTO(s.id ,s.movie.id, s.movie.name, s.movie.imageSmallUrl, s.date, s.time, s.timeEnd) FROM Schedule s JOIN s.showtimes st WHERE st.room.id = :roomId")
        List<ScheduleBeforeMovieLoadDTO> findByRoomId(@Param("roomId") Long roomId);

        @Query(value = "SELECT COUNT(*) FROM schedules s "+
                "INNER JOIN bill_detail bd ON s.schedule_id = bd.schedule_id "+
                "WHERE s.schedule_id = :scheduleId", nativeQuery = true)
        Long countTicketBySchedule(Long scheduleId);

        @Query("SELECT new com.movie_theaters.dto.response.ScheduleManager(" +
                "sch.movie.id, sch.movie.duration, s.room.id, s.room.cinema.id, sch.id, sch.price, " +
                "sch.movie.name, s.room.name, s.room.cinema.name, sch.date, sch.time) "+
                "FROM Schedule sch " +
                "JOIN sch.showtimes s")
        Page<ScheduleManager> getListScheduleManager(Pageable pageable);
}
