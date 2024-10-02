package com.movie_theaters.service.impl;

import com.movie_theaters.dto.request.BillFoodRequest;
import com.movie_theaters.dto.response.BillFoodHistory;
import com.movie_theaters.entity.Bill;
import com.movie_theaters.entity.BillFood;
import com.movie_theaters.entity.Food;
import com.movie_theaters.entity.compositekeyfields.BillFoodId;
import com.movie_theaters.exception.ObjectNotFoundException;
import com.movie_theaters.repository.BillFoodRepository;
import com.movie_theaters.repository.BillRepository;
import com.movie_theaters.repository.FoodRepository;
import com.movie_theaters.service.BillFoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BillFoodServiceImpl implements BillFoodService {
    private final BillFoodRepository billFoodRepository;
    private final BillRepository billRepository;
    private final FoodRepository foodRepository;
    @Override
    @Transactional
    public String saveBillFood(BillFoodRequest request) {
        Optional<Bill> bill = billRepository.findByBillCode(request.getBillCode());

        if (bill.isEmpty()) {
            throw new ObjectNotFoundException("Bill không tồn tại cho bill code: " + request.getBillCode());
        }

        // Chuyển đổi các item trong request thành BillFood và lưu vào cơ sở dữ liệu
        List<BillFood> billFoods = request.getFoods().stream()
                .map(item -> BillFood.builder()
                        .id(new BillFoodId(bill.get().getId(), item.getFoodId()))
                        .bill(bill.get())
                        .food(foodRepository.findById(item.getFoodId()).get())
                        .quantity(item.getQuantity())
                        .build())
                .toList();
        updateQuantityStock(billFoods);
        billFoodRepository.saveAll(billFoods);
        return "Thêm mới hóa đơn thành công!";
    }

    @Override
    public Boolean updateQuantityStock(List<BillFood> billFoods) {
        if (billFoods.isEmpty()) {
            return false;
        }
        try {
            List<Food> foods = new ArrayList<>();
            for (BillFood bill : billFoods) {
                Food food = foodRepository.findById(bill.getFood().getId())
                        .orElseThrow(() -> new ObjectNotFoundException("Đồ ăn không tồn tại có id: " + bill.getFood().getId()));

                int newStock = food.getStock() - bill.getQuantity();
                if (newStock < 0) {
                    throw new IllegalStateException("Không đủ số lượng hàng tồn kho cho food có id:" + food.getId());
                }
                food.setStock(newStock);
                foods.add(food);
            }
            foodRepository.saveAll(foods);
            return true;
        } catch (Exception e) {
            log.error("Xảy ra lỗi khi cập nhật số lượng hàng tồn kho", e);
            return false;
        }
    }
    @Override
    public List<BillFoodHistory> getBillFoodDetail(String billCode) {
        return billFoodRepository.getBillFoodDetail(billCode);
    }
}
