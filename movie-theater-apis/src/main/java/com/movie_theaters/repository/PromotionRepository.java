package com.movie_theaters.repository;

import com.movie_theaters.entity.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Page<Promotion> findAll(Pageable pageable);
    boolean existsByInfo(String info);

}
