package com.movie_theaters.service;

import com.movie_theaters.dto.request.BillFoodRequest;
import com.movie_theaters.dto.response.BillFoodHistory;
import com.movie_theaters.entity.BillFood;

import java.util.List;

public interface BillFoodService {
    String saveBillFood(BillFoodRequest billFoodRequest);
    List<BillFoodHistory> getBillFoodDetail(String billCode);
    Boolean updateQuantityStock(List<BillFood> billFoods);
}
