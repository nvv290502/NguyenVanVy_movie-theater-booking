package com.movie_theaters.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movie_theaters.entity.enums.StatusBill;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private StatusBill status;

    @Column(name = "room_name", length = 50)
    @Nationalized
    private String roomName;

    @Column(name = "cinema_name")
    @Nationalized
    private String cinmaName;

    @Column(name = "time")
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "order")
    @JsonIgnore
    private Set<FoodOrder> foodOrders = new HashSet<>();
}
