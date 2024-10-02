package com.movie_theaters.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "food")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long id;

    @Column(name = "food_name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "stock")
    private int stock;

    @Column(name = "image_food")
    private String image;
    
    @Column(name = "description")
    private String description;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @OneToMany(mappedBy = "food")
    @JsonIgnore
    private Set<BillFood> billFoods = new HashSet<>();

    @OneToMany(mappedBy = "food")
    @JsonIgnore
    private Set<FoodOrder> foodOrders = new HashSet<>();
}
