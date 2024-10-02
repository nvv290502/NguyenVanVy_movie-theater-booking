package com.movie_theaters.service.impl;

import com.movie_theaters.dto.response.BillDetailHistory;
import com.movie_theaters.email.EmailService;
import com.movie_theaters.entity.*;
import com.movie_theaters.entity.compositekeyfields.SeatRoomId;
import com.movie_theaters.entity.enums.StatusBill;
import com.movie_theaters.entity.compositekeyfields.BillDetailId;
import com.movie_theaters.entity.enums.TypeSeat;
import com.movie_theaters.exception.ObjectNotFoundException;
import com.movie_theaters.repository.*;
import com.movie_theaters.service.BillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BillServiceImpl implements BillService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final BillDetailRepository billDetailRepository;
    private final BillRepository billRepository;
    private final SeatRepository seatRepository;
    private final RoomRepository roomRepository;
    private final ShowtimeRepository showtimeRepository;
    private final BillFoodRepository billFoodRepository;
    private final EmailService emailService;
    private final RoomSeatRepository roomSeatRepository;

    @Override
    public Bill saveBill(String billCode, Long userId, Long movieId, Long roomId, LocalDate showDate, LocalTime showTime, String seats, Float amountMoney) {
        Optional<User> user = userRepository.findById(userId);
        Schedule schedule = scheduleRepository.getScheduleByMovieAndShowDateAndSowTime(movieId, showDate, showTime);
        Optional<Room> room = roomRepository.findById(roomId);
        Showtime showtime = showtimeRepository.getShowtimesByRoomAndSchedule(room.get(), schedule);

        Bill bill = new Bill();
        bill.setUser(user.get());
        bill.setAmountMoney(amountMoney);
        bill.setBillCode(billCode);
        bill.setIsTicketIssued(false);
        bill.setStatus(StatusBill.UNPAID);
        billRepository.save(bill); // Lưu Bill để có ID

        List<Seat> seatList = convertStringToSeats(seats);

        for (Seat seat : seatList) {
            Seat foundSeat = seatRepository.findSeatsByColumnNameAndRowName(seat.getColumnName(), seat.getRowName());

            // Tạo đối tượng khóa chính hợp thành
            BillDetailId id = new BillDetailId();
            id.setBillId(bill.getId()); // Gán ID của hóa đơn
            id.setSeatId(foundSeat.getId()); // Gán ID của ghế
            id.setRoomId(room.get().getId());
            id.setScheduleId(schedule.getId());

            BillDetail existingBillDetail = billDetailRepository.getBillDetailByRoomAndSeatAndSchedule(
                    room.get().getId(), foundSeat.getId(), schedule.getId());

            if (existingBillDetail != null) {
                // Nếu đã tồn tại, ném exception để rollback toàn bộ giao dịch
                throw new IllegalArgumentException("Đã có người đặt trước bạn vị trí ghế: " + foundSeat.getRowName()+foundSeat.getColumnName());
            }

            BillDetail billDetail = new BillDetail();
            billDetail.setId(id); // Thiết lập khóa chính
            billDetail.setShowtime(showtime);
            billDetail.setBill(bill);
            billDetail.setSeat(foundSeat);

            billDetailRepository.save(billDetail); // Lưu BillDetail
        }
        return bill;
    }



    // Chuyển đổi chuỗi "A1,A2" thành danh sách ghế
    public static List<Seat> convertStringToSeats(String seatString) {
        List<Seat> seatList = new ArrayList<>();
        String[] seatArray = seatString.split(",");

        for (String seat : seatArray) {
            // Phân tách tên cột và hàng
            String rowName = seat.substring(0, 1); // Cột là ký tự đầu tiên
            String columnName = seat.substring(1); // Hàng là phần còn lại

            // Tạo đối tượng ghế và thêm vào danh sách
            Seat newSeat = new Seat();
            newSeat.setColumnName(columnName);
            newSeat.setRowName(rowName);
            seatList.add(newSeat);
        }
        return seatList;
    }

    @Scheduled(fixedRate = 30000)
    public void deleteBillUnPain() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time = now.minusMinutes(15);
        List<Bill> bills = billRepository.findBillByStatusAndCreatedDateTimeBefore(StatusBill.UNPAID, time);
        List<Long> billIds = bills.stream()
                .map(Bill::getId)
                .collect(Collectors.toList());

        List<BillDetail> billDetails = billIds.stream()
                .flatMap(billId -> billDetailRepository.findByBillId(billId).stream())
                .collect(Collectors.toList());

        log.info("Xóa hóa đơn chưa thanh toán - time: {}", now);
        billDetailRepository.deleteAll(billDetails);
        billRepository.deleteAll(bills);
    }

    @Override
    public Bill updateBillByStatus(String billCode, Float amountMoney) {
        Optional<Bill> bill = billRepository.findByBillCode(billCode);
        if (bill.isEmpty()) {
            throw new ObjectNotFoundException("Bill không tồn tại cho bill code: " + billCode);
        }
        bill.get().setStatus(StatusBill.PAID);
        bill.get().setAmountMoney(amountMoney);
        return billRepository.save(bill.get());
    }

    @Override
    public void sendBill(String billCode, String email) {
        Optional<Bill> bill = billRepository.findByBillCode(billCode);
        if (bill.isEmpty()) {
            throw new ObjectNotFoundException("Bill không tồn tại cho bill code: " + billCode);
        }
        List<BillDetail> billDetails = billDetailRepository.findByBillId(bill.get().getId());
        List<BillFood> billFoods = billFoodRepository.findByBillId(bill.get().getId());

        Long scheduleId = null;
        Long roomId = null;

        for (BillDetail detail : billDetails) {
            scheduleId = detail.getShowtime().getSchedule().getId();
            roomId = detail.getShowtime().getRoom().getId();

        }
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        if(schedule.isEmpty()){
            throw new ObjectNotFoundException("Suất chiếu không tồn tại cho schedule id: " + scheduleId);
        }
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isEmpty()){
            throw new ObjectNotFoundException("Phòng chiếu không tồn tại cho room id: " + roomId);
        }
        emailService.sendBill(email, bill.get(), schedule.get(), billDetails, billFoods, room.get());
    }

    @Override
    public String deleteBillByBillCode(String billCode) {
        if(billCode == null){
            throw new NullPointerException("chưa nhập bill code");
        }
        Optional<Bill> bill = billRepository.findByBillCode(billCode);
        if(bill.isEmpty()){
            throw new ObjectNotFoundException("hóa đơn không tồn tại");
        }
        List<BillDetail> billDetails = billDetailRepository.findByBillId(bill.get().getId());
        billDetailRepository.deleteAll(billDetails);
        billRepository.delete(bill.get());
        return "Xóa thành công hóa đơn có mã:"+billCode;
    }

}
