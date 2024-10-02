package com.movie_theaters.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillFoodHistory {
    private Long foodId;
    private String foodName;
    private Integer quantity;
    private double price;
    private double amountMoney;
}
