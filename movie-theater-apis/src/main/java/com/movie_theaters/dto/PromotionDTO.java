package com.movie_theaters.dto;

import com.movie_theaters.entity.Bill;
import com.movie_theaters.entity.enums.StatusPromotion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionDTO {
    private Long id;

    @NotBlank(message = "Thông tin không được để trống")
    private String info;

//    @NotBlank(message = "Ảnh không được để trống ")
    private String imageUrl;

    @NotNull(message = "Giá trị khuyến mãi không được để trống")
    private Double discount;

    @NotNull(message = "Start date cannot be null")
    private LocalDateTime startDateTime;

    @NotNull(message = "End date cannot be null")
    private LocalDateTime endDateTime;

    @NotBlank(message = "Trạng thái không được để trống")
    private StatusPromotion statusPromotion;

    private List<Long> bills;

}
