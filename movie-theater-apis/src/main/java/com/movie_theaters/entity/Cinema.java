package com.movie_theaters.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/*
 * Author:tungnt
 */

@Entity
@Table(name = "cinemas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinema_id")
    private Long id;

    @Column(name = "cinema_name")
    @Nationalized
    private String name;

    @Column(name = "room_image")
    private String imageUrl;

    @Column(name = "address")
    @Nationalized
    private String address;

    @Column(name = "hotline")
    private String hotline;

    @Column(name = "description")
    @Nationalized
    private String description;

    private Integer rating;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @OneToMany(mappedBy = "cinema")
    @JsonIgnore
    private Set<Room> rooms = new HashSet<>();

}
