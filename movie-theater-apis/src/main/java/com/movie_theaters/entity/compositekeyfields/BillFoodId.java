package com.movie_theaters.entity.compositekeyfields;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BillFoodId implements Serializable {
    private Long billId;
    private Long foodId;
}
