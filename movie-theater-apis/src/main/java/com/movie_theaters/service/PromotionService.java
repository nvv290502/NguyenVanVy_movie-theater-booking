package com.movie_theaters.service;

import com.movie_theaters.dto.MovieDTO;
import com.movie_theaters.dto.PromotionDTO;
import com.movie_theaters.dto.request.PromotionRequest;
import com.movie_theaters.entity.Promotion;
import com.movie_theaters.entity.enums.StatusPromotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface PromotionService {
    Page<PromotionDTO> getAllPromotions(int page, int size, String column, String sortType);
    PromotionDTO savePromotion(PromotionDTO promotionDTO);
    String updatePromotion(PromotionRequest promotionRequest, MultipartFile image);
    PromotionDTO findPromotionById(Long id);
    String updateActivePromotion(Long id, StatusPromotion status);
}
