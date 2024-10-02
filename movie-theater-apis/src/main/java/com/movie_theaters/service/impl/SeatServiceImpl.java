package com.movie_theaters.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import com.movie_theaters.dto.request.SeatLayoutRequest;
import com.movie_theaters.entity.Seat;
import com.movie_theaters.repository.SeatRepository;
import com.movie_theaters.service.SeatService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SeatServiceImpl implements SeatService {
    private SeatRepository seatRepository;

    public SeatServiceImpl(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Override
    public Seat saveSeat(Seat seat) {
        Optional<Seat> seatOpt = seatRepository.findByRowNameAndColumnName(seat.getRowName(), seat.getColumnName());
        if (seatOpt.isPresent()) {
            return seatOpt.get();
        }
        return seatRepository.save(seat);
    }

    @Override
    public List<Seat> saveAllSeat(List<SeatLayoutRequest> request) {
        List<Seat> seatToSave = new ArrayList<>();
        for(SeatLayoutRequest s : request){
            if(!seatRepository.existsByColumnNameAndRowName(s.getColumnName(), s.getRowName())){
                Seat seat = new Seat();
                s.setColumnName(s.getColumnName());
                s.setRowName(s.getRowName());
                seatToSave.add(seat);
            }
        }
        if (!seatToSave.isEmpty()) {
            seatRepository.saveAll(seatToSave);
        }
        return seatToSave;
    }
}
