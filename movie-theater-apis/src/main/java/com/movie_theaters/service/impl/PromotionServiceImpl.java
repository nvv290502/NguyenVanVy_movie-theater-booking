package com.movie_theaters.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie_theaters.dto.PromotionDTO;
import com.movie_theaters.dto.request.PromotionRequest;
import com.movie_theaters.entity.Bill;
import com.movie_theaters.entity.Promotion;
import com.movie_theaters.entity.enums.StatusPromotion;
import com.movie_theaters.exception.EmptyListException;
import com.movie_theaters.exception.ExistObjectException;
import com.movie_theaters.repository.BillRepository;
import com.movie_theaters.repository.PromotionRepository;
import com.movie_theaters.service.PromotionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final BillRepository billRepository;
    private final ImageUploadService imageUploadService;

    @Override
    public PromotionDTO savePromotion(PromotionDTO promotionDTO) {
        if (promotionRepository.existsByInfo(promotionDTO.getInfo())) {
            throw new ExistObjectException("Khuyến mãi đã tồn tại");
        }

        Promotion promotion = new Promotion();
        BeanUtils.copyProperties(promotionDTO, promotion);
        promotion.setStatusPromotion(promotionDTO.getStatusPromotion());

//        // Handle bills
//        Set<Bill> bills = promotionDTO.getBills().stream()
//                .map(billId -> billRepository.findById(billId)
//                        .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại với ID: " + billId)))
//                .collect(Collectors.toSet());
//
//
//        promotion.setBills(bills);
        promotionRepository.save(promotion);
        return promotionDTO;
    }

    @Override
    public Page<PromotionDTO> getAllPromotions(int page, int size, String column, String sortType) {
        Sort sort = Sort.by(column);
        sort = "asc".equalsIgnoreCase(sortType) ? sort.ascending() : sort.descending();
        Page<Promotion> promotions = promotionRepository.findAll(PageRequest.of(page, size, sort));
        if (promotions.isEmpty()) {
            throw new EmptyListException("Không có khuyến mãi nào");
        }

        return promotions.map(promotion -> {
            PromotionDTO promotionDTO = new PromotionDTO();
            BeanUtils.copyProperties(promotion, promotionDTO);
            promotionDTO.setBills(
                    promotion.getBills().stream()
                            .map(Bill::getId)
                            .collect(Collectors.toList())
            );
            return promotionDTO;
        });
    }

    @Override
    public String updatePromotion(PromotionRequest promotionRequest, MultipartFile image) {
        if (promotionRequest.getId() == null) {
            throw new NullPointerException("Bạn chưa nhập id");
        }

        Optional<Promotion> promotion = promotionRepository.findById(promotionRequest.getId());
        if (promotion.isEmpty()) {
            throw new EmptyListException("Khuyến mãi không tồn tại!");
        }
        try{
            if(promotionRequest.getImageUrl() == null) {
                promotionRequest.setImageUrl(imageUploadService.uploadImage(image));
            }
            BeanUtils.copyProperties(promotionRequest, promotion.get());
            promotionRepository.save(promotion.get());
        }catch (IOException ex){
            log.error("lỗi khi xử lý ảnh");
        }
        return "Cập nhật khuyến mãi thành công!";
    }

    @Override
    public PromotionDTO findPromotionById(Long id) {
        PromotionDTO promotionDTO = new PromotionDTO();
        Optional<Promotion> promotion = promotionRepository.findById(id);
        if (promotion.isEmpty()) {
            throw new EmptyListException("Khuyến mãi không tồn tại!");
        }
        BeanUtils.copyProperties(promotion.get(), promotionDTO);
        if(promotion.get().getStatusPromotion() != null){
            promotionDTO.setStatusPromotion(promotion.get().getStatusPromotion());
        }
        return promotionDTO;
    }

    @Override
    public String updateActivePromotion(Long id, StatusPromotion currentStatus) {
        Optional<Promotion> promotion = promotionRepository.findById(id);
        if (promotion.isEmpty()) {
            throw new EmptyListException("Id không tồn tại");
        }

        // Chuyển đổi trạng thái
        StatusPromotion newStatus = currentStatus == StatusPromotion.ACTIVE ? StatusPromotion.INACTIVE : StatusPromotion.ACTIVE;

        // Cập nhật trạng thái mới cho promotion
        promotion.get().setStatusPromotion(newStatus);
        promotionRepository.save(promotion.get());

        return "Trạng thái " + newStatus + " đã được đặt cho Promotion có id [" + id + "] thành công";
    }

}
