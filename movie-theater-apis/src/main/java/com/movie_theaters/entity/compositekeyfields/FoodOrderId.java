package com.movie_theaters.entity.compositekeyfields;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FoodOrderId implements Serializable {
    private Long foodId;
    private Long orderId;
}
