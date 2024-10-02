package com.movie_theaters.entity;

import com.movie_theaters.entity.compositekeyfields.FoodOrderId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "food_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private FoodOrderId id;

    @MapsId("orderId")
    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    @MapsId("foodId")
    @ManyToOne
    @JoinColumn(name = "food_id", insertable = false, updatable = false)
    private Food food;

    private Integer quantity;
}
