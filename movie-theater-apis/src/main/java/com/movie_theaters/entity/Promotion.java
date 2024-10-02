package com.movie_theaters.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movie_theaters.entity.enums.StatusPromotion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table
@Data

public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_id")
    private Long id;

    @Column(name = "info")
    private String info;

    @Column(name = "image_prom")
    private String imageUrl;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "start_datetime")
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime")
    private LocalDateTime endDateTime;
    
    @Column(name = "status_promotion")
    @Enumerated(EnumType.STRING)
    private StatusPromotion statusPromotion;

    @OneToMany(mappedBy = "promotion")
    @JsonIgnore
    private Set<Bill> bills;


}
