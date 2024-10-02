package com.movie_theaters.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movie_theaters.entity.enums.StatusMovie;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long id;
    @Column(name = "name_movie",columnDefinition = "NVARCHAR(255)")
    private String name;
    @Column(name = "summary", columnDefinition = "NVARCHAR(1000)")
    private String summary;
    @Column(name = "duration")
    private Integer duration;
    @Column(name = "release_date", columnDefinition = "DATE")
    private LocalDate releasedDate;
    @Column(name = "author", columnDefinition = "NVARCHAR(255)")
    private String author;
    @Column(name = "actor", columnDefinition = "NVARCHAR(255)")
    private String actor;
    @Column(name = "language", columnDefinition = "NVARCHAR(255)")
    private String language;
    @Column(name = "trailer", columnDefinition = "VARCHAR(255)")
    private String trailerUrl;
    @Column(name = "is_enable")
    private Boolean isEnable;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusMovie status;
    @Column(name = "image_small")
    private String imageSmallUrl;
    @Column(name = "image_large")
    private String imageLargeUrl;
    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;
    @Column(name = "updated_date")
    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "movie")
    @JsonIgnore
    private Set<Schedule> schedules;

    @ManyToMany
    @JoinTable(name = "category_movie",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;

    @OneToMany(mappedBy = "movie")
    @JsonIgnore
    private Set<Review> reviews;

}
