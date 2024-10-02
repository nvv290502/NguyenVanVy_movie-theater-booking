package com.movie_theaters.repository;

import com.movie_theaters.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> getByIsEnable(Pageable pageable, Boolean isEnable);

    Page<Category> findAll(Pageable pageable);
    Boolean existsByName(String name);
    Category getByName(String name);
}
