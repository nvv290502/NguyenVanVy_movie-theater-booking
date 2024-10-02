package com.movie_theaters.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class BillFoodRequest {
    private String billCode;
    private List<BillFoodItem> foods;

    @Data
    public static class BillFoodItem {
        private Long foodId;
        private Integer quantity;
    }
}



