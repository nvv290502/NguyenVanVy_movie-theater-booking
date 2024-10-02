package com.movie_theaters.service.impl;

import com.movie_theaters.dto.ShowTimeDTO;
import com.movie_theaters.dto.common.ScheduleShowtimeDTO;
import com.movie_theaters.entity.Movie;
import com.movie_theaters.entity.Room;
import com.movie_theaters.entity.Schedule;
import com.movie_theaters.entity.Showtime;
import com.movie_theaters.entity.compositekeyfields.ShowtimeId;
import com.movie_theaters.exception.ObjectNotFoundException;
import com.movie_theaters.repository.RoomRepository;
import com.movie_theaters.repository.ScheduleRepository;
import com.movie_theaters.repository.ShowtimeRepository;
import com.movie_theaters.service.ShowTimeService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowTimeServiceImpl implements ShowTimeService {

    private final ShowtimeRepository showtimeRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public List<ShowTimeDTO> getShowTimeByMovie(Long movieId) {
        if (movieId == null) {
            throw new NullPointerException("movie id không được null!");
        }
        return showtimeRepository.getShowTimeByMovie(movieId, LocalDate.now());
    }

    @Override
    public Showtime saveShowtime(ScheduleShowtimeDTO scheduleShowtimeDTO) {
        Showtime showtime = new Showtime();
        ShowtimeId showtimeId = new ShowtimeId(scheduleShowtimeDTO.getScheduleId(), scheduleShowtimeDTO.getRoomId());
        showtime.setId(showtimeId);
        Room room = roomRepository.findById(scheduleShowtimeDTO.getRoomId())
                .orElseThrow(() -> new RuntimeException("Phòng chiếu phim không tồn tại."));

        showtime.setRoom(room);
        Schedule schedule = scheduleRepository.findById(scheduleShowtimeDTO.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Schedule không tồn tại."));
        showtime.setSchedule(schedule);
        return showtimeRepository.save(showtime);
    }

    @Override
    public String deleteShowtime(Long scheduleId, Long roomId) {
        Long countTicket = scheduleRepository.countTicketBySchedule(scheduleId);
        if(countTicket > 0){
            throw new IllegalArgumentException("Không thể thay đổi suất chiếu đã có người đặt vé!");
        }
        if (!showtimeRepository.existsById(new ShowtimeId(scheduleId, roomId))) {
            throw new ObjectNotFoundException("Lịch chiếu không tồn tại!");
        }
        ShowtimeId showtimeId = new ShowtimeId(scheduleId, roomId);
        showtimeRepository.deleteById(showtimeId);
        return "Hủy lịch chiếu thành công!";
    }
}
