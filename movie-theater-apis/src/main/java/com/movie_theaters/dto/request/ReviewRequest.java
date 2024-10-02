package com.movie_theaters.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private Long movieId;
    private Long userId;
    @NotNull(message = "Vui lòng chọn số sao!")
    private Integer numberStar;
    private String comment;
}
