package com.movie_theaters.service.impl;

import com.movie_theaters.dto.response.BillDetailHistory;
import com.movie_theaters.entity.RoomSeat;
import com.movie_theaters.entity.compositekeyfields.SeatRoomId;
import com.movie_theaters.entity.enums.TypeSeat;
import com.movie_theaters.exception.ObjectNotFoundException;
import com.movie_theaters.repository.BillDetailRepository;
import com.movie_theaters.repository.RoomSeatRepository;
import com.movie_theaters.service.BillDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BillDetailServiceImpl implements BillDetailService {

    private final BillDetailRepository billDetailRepository;
    private final RoomSeatRepository roomSeatRepository;
    @Override
    public List<Long> getSeatByBillDetail(Long movieId, Long roomId, LocalDate showDate, LocalTime showTime, Long userId) {
        return billDetailRepository.getSeatByBillDetail(movieId, roomId, showDate, showTime, userId);
    }
    @Override
    public List<BillDetailHistory> getBillDetail(String billCode) {
        List<BillDetailHistory> list = billDetailRepository.getBillDetail(billCode);
        for(BillDetailHistory bill : list){
            SeatRoomId seatRoomId = new SeatRoomId(bill.getRoomId(), bill.getSeatId());
            Optional<RoomSeat> roomSeat = roomSeatRepository.findById(seatRoomId);
            if(roomSeat.isEmpty()){
                throw new ObjectNotFoundException("RoomSeat không tồn tại!");
            }
            if(roomSeat.get().getTypeSeat().equals(TypeSeat.VIP)){
                bill.setPriceTicket(bill.getPriceTicket() * 1.30);
            }
            bill.setTypeSeat(roomSeat.get().getTypeSeat());
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getMovieTicketStats() {
        List<Object> results = billDetailRepository.getNumberTicketByMovie();

        return results.stream().map(result -> {
            Object[] row = (Object[]) result;
            Map<String, Object> map = Map.of(
                    "movieId", row[0],
                    "movieName", row[1],
                    "numberTicket", row[2]
            );
            return map;
        }).collect(Collectors.toList());
    }
}
