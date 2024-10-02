package com.movie_theaters.service.impl;

import com.movie_theaters.dto.common.ScheduleBeforeMovieLoadDTO;
import com.movie_theaters.dto.common.ScheduleEarlyDTO;
import com.movie_theaters.dto.response.ScheduleManager;
import com.movie_theaters.entity.Movie;
import com.movie_theaters.entity.Room;
import com.movie_theaters.entity.Schedule;
import com.movie_theaters.entity.Showtime;
import com.movie_theaters.entity.compositekeyfields.SeatRoomId;
import com.movie_theaters.exception.ObjectNotFoundException;
import com.movie_theaters.repository.MovieRepository;
import com.movie_theaters.repository.RoomRepository;
import com.movie_theaters.repository.ScheduleRepository;
import com.movie_theaters.repository.ShowtimeRepository;
import com.movie_theaters.service.ScheduleService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Override
    public List<Schedule> getScheduleByCinema(Long movieId, String city, LocalDate date, Long cinemaId) {
        return scheduleRepository.getScheduleByCinema(movieId, city, date, cinemaId, LocalDate.now());
    }

    @Override
    public Schedule getPriceTicket(Long movieId, LocalDate showDate, LocalTime showTime) {
        return scheduleRepository.getScheduleByMovieAndShowDateAndSowTime(movieId, showDate, showTime);
    }

    @Override
    public Schedule saveSchedule(ScheduleEarlyDTO scheduleEarlyDTO) {

        Schedule schedule = new Schedule();
        Movie movie = movieRepository.findById(scheduleEarlyDTO.getMovieId())
                .orElseThrow(() -> new RuntimeException("Phim không tồn tại."));
        schedule.setMovie(movie);
        schedule.setDate(scheduleEarlyDTO.getDate());
        schedule.setTime(scheduleEarlyDTO.getTime());
        schedule.setTimeEnd(scheduleEarlyDTO.getTimeEnd());
        schedule.setPrice(0.00);
        return scheduleRepository.save(schedule);
    }

    @Override
    public Schedule updateSchedule(ScheduleEarlyDTO scheduleEarlyDTO) {
        Long id = Long.valueOf(scheduleEarlyDTO.getScheduleId());
        Long countTicket = scheduleRepository.countTicketBySchedule(id);
        if(countTicket > 0){
            throw new IllegalArgumentException("Không thể thay đổi suất chiếu đã có người đặt!");
        }
        Optional<Schedule> schedule = scheduleRepository.findById(id);
        if (schedule.isEmpty()){
            throw new ObjectNotFoundException("schedule khong ton tai");
        }
        schedule.get().setDate(scheduleEarlyDTO.getDate());
        schedule.get().setTime(scheduleEarlyDTO.getTime());
        schedule.get().setTimeEnd(scheduleEarlyDTO.getTimeEnd());
        return scheduleRepository.save(schedule.get());
    }

    @Override
    public Boolean checkExistSchedule(ScheduleEarlyDTO scheduleEarlyDTO) {
        return scheduleRepository.existsByMovieIdAndDateAndTime(
                scheduleEarlyDTO.getMovieId(),
                scheduleEarlyDTO.getDate(),
                scheduleEarlyDTO.getTime());
    }

    @Override
    public Schedule getExistSchedule(ScheduleEarlyDTO scheduleEarlyDTO) {
        return scheduleRepository.findByMovieIdAndDateAndTime(
                scheduleEarlyDTO.getMovieId(),
                scheduleEarlyDTO.getDate(),
                scheduleEarlyDTO.getTime());
    }

    @Override
    public List<ScheduleBeforeMovieLoadDTO> getSchedule(Long roomId) {
        return scheduleRepository.findByRoomId(roomId);
    }

    @Override
    public Page<ScheduleManager> getListScheduleManager(int page, int size, String column, String sortType) {
        Sort sort = Sort.by(column);
        if ("desc".equalsIgnoreCase(sortType)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        return scheduleRepository.getListScheduleManager(PageRequest.of(page, size, sort));
    }

    @Override
    public String updatePrice(Long scheduleId, Double price) {
        Optional<Schedule> schedule =  scheduleRepository.findById(scheduleId);
        if(schedule.isEmpty()){
            throw new ObjectNotFoundException("Suất chiếu không tồn tại!");
        }
        schedule.get().setPrice(price);
        scheduleRepository.save(schedule.get());
        return "Cập nhật giá vé thành công!";
    }

}
