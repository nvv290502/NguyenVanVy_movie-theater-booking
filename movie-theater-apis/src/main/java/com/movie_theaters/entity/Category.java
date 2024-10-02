package com.movie_theaters.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    @Column(name = "category_name",columnDefinition = "NVARCHAR(255)")
    private String name;
    @Column(name = "description", columnDefinition = "NVARCHAR(1000)")
    private String description;
    @Column(name = "is_enable")
    private Boolean isEnable;
}
