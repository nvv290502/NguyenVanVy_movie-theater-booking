package com.movie_theaters.service.impl;

import com.movie_theaters.dto.CinemaDTO;
import com.movie_theaters.dto.MovieDTO;
import com.movie_theaters.dto.request.CinemaRequest;
import com.movie_theaters.entity.Cinema;
import com.movie_theaters.entity.Movie;
import com.movie_theaters.exception.EmptyListException;
import com.movie_theaters.exception.ObjNotFoundException;
import com.movie_theaters.exception.ObjectNotFoundException;
import com.movie_theaters.repository.CinemaRepository;
import com.movie_theaters.service.CinemaService;

import com.movie_theaters.service.generic.Pagination;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/*
 * Author:tungnt
 */

@Service
@Transactional
public class CinemaServiceImpl extends Pagination<Cinema, Long, CinemaDTO> implements CinemaService {

    private final CinemaRepository cinemaRepository;
    private final ImageUploadService imageUploadService;

    @Autowired
    protected CinemaServiceImpl(JpaRepository<Cinema, Long> repository, CinemaRepository cinemaRepository, ImageUploadService imageUploadService) {
        super(repository);
        this.cinemaRepository = cinemaRepository;
        this.imageUploadService = imageUploadService;
    }

    @Override
    public List<Cinema> findAllCinema() {
        return cinemaRepository.findAll();
    }

    @Override
    public Cinema findCinemaById(Long id) {
        Optional<Cinema> cinemaOpt = cinemaRepository.findById(id);
        if (cinemaOpt.isEmpty()) {
            throw new ObjNotFoundException("Rạp chiếu phim với ID " + id + " ko tồn tại.");
        }
        return cinemaOpt.get();
    }

    @Override
    public Cinema saveCinema(CinemaRequest request, MultipartFile imageUrl) {
        Cinema cinema = new Cinema();
        BeanUtils.copyProperties(request, cinema);
        if(imageUrl != null){
            try {
                cinema.setImageUrl(imageUploadService.uploadImage(imageUrl));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        cinema.setIsEnabled(true);
        return cinemaRepository.save(cinema);
    }

    @Override
    public Cinema updateCinema(Long cinemaId, CinemaRequest request, MultipartFile imageUrl) {
        Optional<Cinema> cinema = cinemaRepository.findById(cinemaId);
        if(cinema.isEmpty()){
            throw new ObjectNotFoundException("Rạp không tồn tại!");
        }
        BeanUtils.copyProperties(request, cinema.get());
        if(imageUrl != null){
            try {
                cinema.get().setImageUrl(imageUploadService.uploadImage(imageUrl));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        cinema.get().setIsEnabled(true);
        return cinemaRepository.save(cinema.get());
    }

    @Override
    public Cinema deactiveCinema(Long cinemaId) {
        Optional<Cinema> cinemaOpt = cinemaRepository.findById(cinemaId);
        if (cinemaOpt.isEmpty()) {
            throw new ObjNotFoundException("Rạp chiếu phim với ID " + cinemaId + " ko tồn tại.");
        }
        Cinema existingCinema = cinemaOpt.get();
        existingCinema.setIsEnabled(false);
        return cinemaRepository.save(existingCinema);
    }

    @Override
    public Cinema activeCinema(Long cinemaId) {
        Optional<Cinema> cinemaOpt = cinemaRepository.findById(cinemaId);
        if (cinemaOpt.isEmpty()) {
            throw new ObjNotFoundException("Rạp chiếu phim với ID " + cinemaId + " ko tồn tại.");
        }
        Cinema existingCinema = cinemaOpt.get();
        existingCinema.setIsEnabled(true);
        return cinemaRepository.save(existingCinema);
    }

    @Override
    public List<Cinema> getCinemaByMovieShowTime(Long movieId, String city, LocalDate showDate) {
        return cinemaRepository.getCinemaByMovieShowTime(movieId, city, showDate, LocalDate.now());
    }

    @Override
    protected CinemaDTO convert(Cinema cinema) {
        CinemaDTO cinemaDTO = new CinemaDTO();
        BeanUtils.copyProperties(cinema, cinemaDTO);
        return cinemaDTO;
    }

    @Override
    protected Page<Cinema> getIsEnabledMethod(PageRequest pageRequest, Boolean isEnable) {
        return cinemaRepository.getByIsEnabled(pageRequest, isEnable);
    }

    public List<Cinema> findAllActiveCinema() {
        return cinemaRepository.findAllByIsEnabledTrue();
    }

    @Override
    public String updateIsEnable(Long id, Boolean isEnable) {
        Optional<Cinema> cinema = cinemaRepository.findById(id);
        if (cinema.isEmpty()) {
            throw new EmptyListException("Id không tồn tài");
        }
        cinema.get().setIsEnabled(isEnable);
        cinemaRepository.save(cinema.get());
        if (!isEnable) {
            return "Cập nhật trạng thái DISABLE rạp có id [" + id + "] thành công!";
        } else {
            return "Cập nhật trạng thái ENABLE rạp có id [" + id + "] thành công!";

        }
    }
    @Override
    public CinemaDTO searchCinemaByName(String name) {
        CinemaDTO cinemaDTO = new CinemaDTO();
        if (name.isBlank()) {
            throw new EmptyListException("Tên rạp không được để trống!");
        }
        Cinema cinema = cinemaRepository.getByName(name);
        BeanUtils.copyProperties(cinema, cinemaDTO);
        return cinemaDTO;
    }

}
