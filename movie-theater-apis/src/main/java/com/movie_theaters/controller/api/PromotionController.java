package com.movie_theaters.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie_theaters.dto.PromotionDTO;

import com.movie_theaters.dto.request.PromotionRequest;
import com.movie_theaters.dto.response.ApiResponse;
import com.movie_theaters.dto.response.ErrorApiResponse;
import com.movie_theaters.entity.Promotion;
import com.movie_theaters.entity.enums.StatusPromotion;
import com.movie_theaters.service.PromotionService;
import com.movie_theaters.service.impl.ImageUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/pub/")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PromotionController {

    private static final Logger log = LoggerFactory.getLogger(PromotionController.class);
    private final PromotionService promotionService;
    private final ImageUploadService imageUploadService;

    @PostMapping("/promotion")
    public ResponseEntity<?> savePromotion(@Valid @ModelAttribute PromotionRequest promotionRequest,
                                           @RequestParam(value = "image", required = false) MultipartFile image) {
        log.info("Request:{}", promotionRequest);
        PromotionDTO promotionDTO = new PromotionDTO();

        try{
            BeanUtils.copyProperties(promotionRequest, promotionDTO);
            promotionDTO.setImageUrl(imageUploadService.uploadImage(image));
        }catch (IOException e){
            return new ResponseEntity<>(new ErrorApiResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), e.getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED, promotionService.savePromotion(promotionDTO), "Thêm phim thành công!"), HttpStatus.CREATED);

    }

    @PutMapping("/promotion")
    public ResponseEntity<String> updatePromotion(@Valid @ModelAttribute PromotionRequest promotionRequest,
                                                  @RequestParam(value = "image", required = false) MultipartFile image) {
        String response = promotionService.updatePromotion(promotionRequest, image);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/promotion")
    public ResponseEntity<Page<PromotionDTO>> getAllPromotions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortType) {

        Page<PromotionDTO> promotions = promotionService.getAllPromotions(page, size, sortBy, sortType);
        return ResponseEntity.ok(promotions);
    }

    @GetMapping("/promotion/{id}")
    public ResponseEntity<?> getPromotionById(@PathVariable Long id) {
        if(id == null || id < 0){
            return new ResponseEntity<>("Id không hợp lệ", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK,promotionService.findPromotionById(id), "Trả về khuyến mãi theo id thành công"), HttpStatus.OK);

    }

    @PostMapping("/promotion/ACTIVE")
    public ResponseEntity<ApiResponse<String>> setActive(@RequestParam Long id,
                                                         @RequestParam  StatusPromotion status){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.ACCEPTED, null, promotionService.updateActivePromotion(id,status)), HttpStatus.ACCEPTED);
    }


}